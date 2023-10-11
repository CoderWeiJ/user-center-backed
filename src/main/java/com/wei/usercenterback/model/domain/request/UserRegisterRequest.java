package com.wei.usercenterback.model.domain.request;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author CoderWeiJ
 */
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 4126753428215211245L;

    public String userAccount;
    public String userPassword;
    public String checkPassword;
}
