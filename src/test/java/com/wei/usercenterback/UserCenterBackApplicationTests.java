package com.wei.usercenterback;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class UserCenterBackApplicationTests {

    @Test
    void testDigest() throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        String s = DigestUtils.md5DigestAsHex(("wodiuleiloumou" + "mypassword").getBytes());
        System.out.println("密码：" + s);

    }

    @Test
    void contextLoads() {
    }

}
