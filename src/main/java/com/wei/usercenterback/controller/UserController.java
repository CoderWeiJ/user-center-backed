package com.wei.usercenterback.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wei.usercenterback.constant.UserConstant;
import com.wei.usercenterback.model.domain.User;
import com.wei.usercenterback.model.domain.request.UserLoginRequest;
import com.wei.usercenterback.model.domain.request.UserRegisterRequest;
import com.wei.usercenterback.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口
 *
 * @author CoderWeiJ
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public long userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) return -1;
        if (StringUtils.isAnyBlank(userRegisterRequest.userAccount, userRegisterRequest.userPassword, userRegisterRequest.checkPassword))
            return -1;


        return userService.userRegister(userRegisterRequest.userAccount, userRegisterRequest.userPassword, userRegisterRequest.checkPassword);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 参数实体
     * @param request          请求体
     * @return 脱敏后的用户对象
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 校验
        if (userLoginRequest == null) return null;
        if (StringUtils.isAnyBlank(userLoginRequest.userAccount, userLoginRequest.userPassword))
            return null;


        return userService.userLogin(userLoginRequest.userAccount, userLoginRequest.userPassword, request);
    }

    /**
     * 用户查询
     *
     * @param username 用户昵称
     * @return 用户列表
     */
    @GetMapping("/query")
    public List<User> userQuery(String username, HttpServletRequest request) {
        if (!StringUtils.isNotBlank(username)) {
            return new ArrayList<>();
        }

        // 鉴权，不是管理员返回空数组
        if (!isAdmin(request)) return new ArrayList<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.like("username", username);

        return userService.list(queryWrapper);
    }

    /**
     * 删除用户（逻辑删除）
     *
     * @param id 用户id
     * @return 布尔值
     */
    @DeleteMapping("/delete")
    public boolean userDelete(long id, HttpServletRequest request) {
        if (id < 0 || !isAdmin(request)) return false;
        return userService.removeById(id);
    }

    private boolean isAdmin(HttpServletRequest request) {
        User userObj = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        return userObj != null && userObj.getRole() == UserConstant.ADMIN_ROLE;
    }
}
