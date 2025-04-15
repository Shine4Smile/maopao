package com.simple.maopao.service;

import cn.hutool.core.date.StopWatch;
import com.simple.maopao.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@SpringBootTest
public class InsertTestUser {
    @Resource
    private UserService userService;

    @Test
    public void doInsertTestUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("批量插入测试数据");
        final int INSERT_NUM = 10000000;
        List<User> list = new ArrayList<>();
        for (int i = 1904322; i < INSERT_NUM; i++) {
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
            list.add(user);
        }
        userService.saveBatch(list, 1000);

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
//        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }

    @Test
    public void doConcurrencyInsertTestUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("批量插入测试数据");
        final int INSERT_NUM = 10000000;
        int j = 0;
        List<CompletableFuture> listFuture = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<User> list = new ArrayList<>();
            while (true) {
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
                list.add(user);
                if (j % 10000 == 0) {
                    break;
                }
            }
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                userService.saveBatch(list, 1000);
            });
            listFuture.add(future);
        }
        CompletableFuture.allOf(listFuture.toArray(new CompletableFuture[]{})).join();
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeSeconds());
//        System.out.println(stopWatch.prettyPrint(TimeUnit.SECONDS));
    }
}
