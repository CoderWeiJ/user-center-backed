package com.wei.usercenterback.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.usercenterback.constant.UserConstant;
import com.wei.usercenterback.service.UserService;
import com.wei.usercenterback.model.domain.User;
import com.wei.usercenterback.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wei
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-10-10 17:24:44
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {


    /**
     * 热值，混淆密码
     */
    private static final String sALT = "coderweij";


    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        // 传入字段是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            System.out.println("账号密码等为空");
            // 修改为自定义异常 todo...
            return -1;
        }
        userAccount = userAccount.trim();
        userPassword = userPassword.trim();
        // 校验账号和密码长度
        if (userAccount.length() < 4) {
            System.out.println("账号长度必须>=4！");
            return -1;
        }

        if (userPassword.length() < 6) {
            System.out.println("密码长度必须>=6！");
            return -1;
        }

        // 账号不能出现特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("账号只能由英文字母、数字、下划线组成！" + userAccount.matches("^[a-zA-Z0-9_]+$"));
            return -1;
        }


        // 密码和校验密码是否相同
        if (!userPassword.equals(checkPassword)) {
            System.out.println("密码和校验密码必须一致！");
            return -1;
        }

        // 用户不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        if (this.count(queryWrapper) > 0) {
            System.out.println("账号不能重复");
            return -1;
        }

        // 2. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((sALT + userPassword).getBytes());
        // 3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);

        boolean res = this.save(user);
        if (!res) {
            System.out.println("数据插入失败！");
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        // 传入字段是否为空 待优化 todo...
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            System.out.println("账号密码等为空");
            return null;
        }
        userAccount = userAccount.trim();
        userPassword = userPassword.trim();
        // 校验账号和密码长度
        if (userAccount.length() < 4) {
            System.out.println("账号长度必须>=4！");
            return null;
        }

        if (userPassword.length() < 6) {
            System.out.println("密码长度必须>=6！");
            return null;
        }

        // 账号不能出现特殊字符
        if (!userAccount.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("账号只能由英文字母、数字、下划线组成！" + userAccount.matches("^[a-zA-Z0-9_]+$"));
            return null;
        }

        // 2. 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((sALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword!");
            System.out.println("账号或密码错误！");
            return null;
        }

        // 3. 用户脱敏
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setRole(user.getRole());
        safetyUser.setCreatedTime(user.getCreatedTime());


        // 4. 记录session（用户信息）
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safetyUser);

        return safetyUser;
    }
}




