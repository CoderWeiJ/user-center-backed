package com.wei.usercenterback.service;

import java.util.Date;


import com.wei.usercenterback.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author CoderWeiJ
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void testAddUser() {
        User user = new User();
        user.setUsername("coderweij");
        user.setUserAccount("coderweij");
        user.setAvatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/aRneVaen8XSWOILibfkW5SHicYZia2oDxA8zKUtnzLRVFbwiclEK8f80QMRk3kviawzL8gTWk4MgA0P0VeF1r0O0upg/132");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("1008611");
        user.setEmail("1008611@foxmail.com");

        boolean res = userService.save(user);

        System.out.println("新插入数据的id：" + user.getId());

        // 断言结果为true，不是则报错
        Assertions.assertTrue(res);

    }

    @Test
    void userRegister() {
        String userAccount = "12341234";
        String userPassword = " ";
        String checkPassword = "12345678";

        // 空密码测试
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 密码和校验密码不一致
        userPassword = "12345678";
        checkPassword = "123456789";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertEquals(-1, result);

        // 账号名包含特殊字符
        userAccount = "f123cA_rr";
        userPassword = "12345678";
        checkPassword = "12345678";
        result = userService.userRegister(userAccount, userPassword, checkPassword);
        Assertions.assertTrue(result > 0);
    }
}