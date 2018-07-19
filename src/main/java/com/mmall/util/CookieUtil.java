package com.mmall.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN = "www.happymmall.com";
    private final static String COOKIE_NAME = "mmall_login_token";

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie :
                    cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    response.addCookie(cookie);
                    return;
                }
            }
        }

    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie :
                    cookies) {
                log.info("read cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                if (COOKIE_NAME.equals(cookie.getName())) {
                    log.info("return cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(COOKIE_DOMAIN);
        //表示设置在根目录,如果path设置为"test",
        // 那么只有test目录下或者test子目录下的页面才能访问到该cookie
        cookie.setPath("/");
        //单位:秒 Second 如果设置为-1,代表永久
        //如果不设置maxAge,cookie就不会写入硬盘,而是写在内存当中,只在当前页面有效
        cookie.setMaxAge(60 * 60 * 24 * 365);
        log.info("write cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());
        response.addCookie(cookie);
    }

}
