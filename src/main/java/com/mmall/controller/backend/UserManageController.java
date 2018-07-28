package com.mmall.controller.backend;

import com.mmall.common.Cons;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardRedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author zs595
 */
@RestController
@RequestMapping("/manage/user/")
public class UserManageController {

    private IUserService iUserService;

    @Autowired
    public UserManageController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpServletResponse httpServletResponse, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Cons.Role.ROLE_AMDIN) {
                //登录的是管理员
                //新增redis共享Cookie
                CookieUtil.writeLoginToken(httpServletResponse, session.getId());
                ShardRedisPoolUtil.setExpire(session.getId(), JsonUtil.objToString(user), Cons.RedisCacheExtime.REDIS_SESSION_EXTIME);
            }else {
                return ServerResponse.createByErrorMessage("不是管理员,无法登录");
            }
        }
        return response;
    }
}
