package com.mmall.common;

public class Cons {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public static final String TOKEN_PERFIX = "token_";

    public interface Role {
        /**
         * 普通用户
         */
        int ROLE_CUSTOME = 0;
        //
        /**
         * 管理员
         */
        int ROLE_AMDIN = 1;
    }
}
