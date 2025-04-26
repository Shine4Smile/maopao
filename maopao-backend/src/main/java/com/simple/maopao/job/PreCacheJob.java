package com.simple.maopao.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.simple.maopao.model.domain.User;
import com.simple.maopao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 预热缓存
 *
 * @author Simple
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> defaultRedisTemplate;
    @Resource
    private RedissonClient redissonClient;

    // 先初步实现对重点用户首页进行缓存

    /**
     * 每日天执行一次，预热缓存默认推荐用户列表信息
     * synchronized只针对单个jvm有效
     */
    @Scheduled(cron = "0 59 23 * * *")
    public void doCacheRecommendUser() {
        RLock lock = redissonClient.getLock("maopao:precache:docache:lock");
        try {
            // 只有一个线程能获取锁，所以等待时间必须为0
            // 使用redisson看门狗（默认时间30s,每10秒自动续期）机制，时不能指定过期时间。当前进程挂掉（debug也会被当作宕机）看门狗线程也会停止不会自动续期
            if (lock.tryLock(0, -1, TimeUnit.SECONDS)) {
                String key = "maopao:user:recommend";
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                Page<User> userList = userService.page(new Page<>(1, 20), queryWrapper);
                List<User> safeList = userList.getRecords().stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
                userList.setRecords(safeList);
                // 写入缓存
                try {
                    defaultRedisTemplate.opsForValue().set(key, userList, 3 * 60 * 60, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error("redis存储用户列表失败！");
                }
            } else {
                log.warn("doCacheRecommendUser任务已被进程占用！");
            }
        } catch (InterruptedException e) {
            log.error("预缓存异常");
        } finally {
            // 避免释放掉其他线程的锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
