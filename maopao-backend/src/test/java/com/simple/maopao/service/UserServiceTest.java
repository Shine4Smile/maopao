package com.simple.maopao.service;


import com.simple.maopao.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * 用户服务测试
 *
 * @author Simple
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    /**
     * 测试添加用户
     */
    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserName("zs");
        user.setUserAccount("123");
        user.setAvatarUrl("https://img.icons8.com/?size=512&id=Va7lNc72i71b&format=png");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("456");
        boolean result = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(result);
    }


    /**
     * 测试更新用户
     */
    @Test
    public void testUpdateUser() {
//        User user = new User();
//        user.setId(1L);
//        user.setUserName("zs");
//        user.setUserAccount("123");
//        user.setAvatarUrl("https://img.icons8.com/?size=512&id=Va7lNc72i71b&format=png");
//        user.setGender(0);
//        user.setUserPassword("xxx");
//        user.setPhone("123");
//        user.setEmail("456");
//        boolean result = userService.updateById(user);
//        Assertions.assertTrue(result);
    }

    /**
     * 测试删除用户
     */
    @Test
    public void testDeleteUser() {
//        boolean result = userService.removeById(1L);
//        Assertions.assertTrue(result);
    }

    // https://space.bilibili.com/12890453/

    /**
     * 测试获取用户
     */
    @Test
    public void testGetUser() {
//        User user = userService.getById(1L);
//        Assertions.assertNotNull(user);
    }

    /**
     * 测试用户注册
     */
    @Test
    void userRegister() {
//        String userAccount = "zs";
//        String userPassword = "123456";
//        String checkPassword = "123456";
//        String planetCode = "1";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "mao";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "zs";
//        userPassword = "123456";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "mao pao";
//        userPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "zs";
//        checkPassword = "12345678";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
//        userAccount = "zs";
//        result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
//        Assertions.assertEquals(-1, result);
    }
}