package com.mmall.controller.common.interceptor;

import com.mmall.common.Cons;
import com.mmall.common.ServerResponse;
import com.mmall.controller.backend.ProductManageController;
import com.mmall.pojo.User;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardRedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    /**
     * 在进入Controller之前执行
     *
     * @param request  用户请求
     * @param response 响应用户请求
     * @param handler  请求内容详情信息
     * @return 是否被拦截
     * @throws Exception 抛出异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        String className = handlerMethod.getBean().getClass().getName();
        String methodName = handlerMethod.getMethod().getName();
        //解析具体的参数,请求的具体参数的key-value,用于打印日志
        StringBuffer requestParameterBuffer = new StringBuffer();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String key :
                parameterMap.keySet()) {
            requestParameterBuffer.append(key).append("=").append(Arrays.toString(parameterMap.get(key))).append(" ");
        }
        log.info("权限拦截的请求的参数,ClassName:{},MethodName:{},parameterMap:{}", className, methodName, requestParameterBuffer);

        User user = null;
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isNotEmpty(loginToken)) {
            String userJsonStr = ShardRedisPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJsonStr, User.class);
        }
        if (user == null || Cons.Role.ROLE_AMDIN != user.getRole()) {
            //这里需要添加response.reset()否则会报异常 getWriter() has already been called for this response;
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            if (user == null) {
                if (ProductManageController.class.getName().equals(className) && "richtextImgUpload".equals(methodName)) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户未登录");
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("用户未登录")));
                }
            }else {
                if (ProductManageController.class.getName().equals(className) && "richtextImgUpload".equals(methodName)) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("success", false);
                    resultMap.put("msg", "用户无权限操作");
                    out.print(JsonUtil.objToString(resultMap));
                }else {
                    out.print(JsonUtil.objToString(ServerResponse.createByErrorMessage("用户无权限操作")));
                }
            }
            out.flush();
            out.close();
            return false;
        }

        return true;
    }

    /**
     * 在Controller结束后执行
     *
     * @param request      用户请求
     * @param response     响应用户请求
     * @param handler      请求内容详情信息
     * @param modelAndView 服务器响应结果
     * @throws Exception 抛出异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    /**
     * 在视图呈现后处理
     *
     * @param request  用户请求
     * @param response 响应用户请求
     * @param handler  请求内容详情信息
     * @param ex       异常信息处理
     * @throws Exception 抛出异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
