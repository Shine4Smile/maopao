package com.simple.maopao.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() {
        RList<Object> testList = redissonClient.getList("testList");
        testList.add("张三");
        System.out.println(testList.get(0));
    }
}
