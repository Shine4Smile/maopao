package com.simple.maopao.once;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.StopWatch;
import com.simple.maopao.mapper.UserMapper;
import com.simple.maopao.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InsertTestUser {
    @Resource
    private UserMapper userMapper;

//    @Scheduled
    public void doInsertTestUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("批量插入测试数据");
        final int INSERT_NUM = 10000000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUserName("测试用户" + i);
            user.setUserAccount("测试用户" + i);
            user.setAvatarUrl("https://img.icons8.com/?size=512&id=Va7lNc72i71b&format=png");
            user.setGender(0);
            user.setUserPassword("047e6040179b5d1bf6ff89dd07b7e652");
            user.setPhone("10086");
            user.setEmail("10086@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("101");
            user.setTags("[]");
            user.setProfile("测试用户" + i + "的简介");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
//        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }
}
