package com.wei.usercenterback.model.domain.request;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author CoderWeiJ
 */
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 742846776168675160L;
    public String userAccount;
    public String userPassword;
}
