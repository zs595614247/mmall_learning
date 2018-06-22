package com.mmall.common;

import com.google.common.collect.Sets;

import java.util.Set;

public class Cons {
    public static final String CURRENT_USER = "currentUser";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface ProductListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_asc","price_desc");
    }

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

    public enum ProductStatusEnum {
        /**
         * 商品状态
         */
        ON_SALE(1,"在线");

        private String value;
        private int code;



        ProductStatusEnum(int code,String value) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public int getCode() {
            return code;
        }
    }
}
