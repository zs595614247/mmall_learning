package com.mmall.controller.portal;

import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardRedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("login.do")
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse response) {
        ServerResponse<User> serverResponse = iUserService.login(username, password);
        if (serverResponse.isSuccess()) {
            CookieUtil.writeLoginToken(response,session.getId());
            ShardRedisPoolUtil.setExpire(session.getId(), JsonUtil.objToString(serverResponse.getData()), Cons.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return serverResponse;
    }
    @PostMapping("logout.do")
    public ServerResponse<User> logout(HttpServletRequest request,HttpServletResponse response) {
        CookieUtil.delLoginToken(request, response);
        ShardRedisPoolUtil.del(CookieUtil.readLoginToken(request));
        return ServerResponse.createBySuccess();
    }

    @PostMapping("register.do")
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    @PostMapping("check_valid.do")
    public ServerResponse<String> checkValid(String str,String type) {
        return iUserService.checkValid(str, type);
    }

    @PostMapping("get_user_info.do")
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
    }

    @PostMapping("forget_get_question.do")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    @PostMapping("forget_check_answer.do")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @PostMapping("forget_reset_password.do")
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPasswrd(username, passwordNew, forgetToken);
    }

    @PostMapping("reset_password.do")
    public ServerResponse<String> resetPassword(HttpServletRequest request,String passwordOld,String passwordNew) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }

    @PostMapping("update_information.do")
    public ServerResponse<User> updateInformation(HttpServletRequest request, User user,HttpServletResponse servletResponse) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User currentUser = JsonUtil.string2Obj(userJsonStr, User.class);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(currentUser.getUsername());
            ShardRedisPoolUtil.setExpire(loginToken, JsonUtil.objToString(response.getData()), Cons.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @PostMapping("get_information.do")
    public ServerResponse<User> getInformation(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,需要强制登录");
        }
        return iUserService.getInformation(user.getId());
    }
}
