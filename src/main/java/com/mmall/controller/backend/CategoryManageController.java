package com.mmall.controller.backend;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardRedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    @PostMapping("add_category.do")
    public ServerResponse addCategory(HttpServletRequest request, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.addCategory(categoryName, parentId);
        }
        return ServerResponse.createByErrorMessage("无权限,需要管理员权限");
    }

    @PostMapping("set_category_name.do")
    public ServerResponse setCategoryName(HttpServletRequest request, String categoryName, Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            return iCategoryService.updateCategoryName(categoryName, categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限,需要管理员权限");
    }

    @PostMapping("get_category.do")
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询子节点的category信息,并且平级无递归
            return iCategoryService.getChildrenParallelCategory(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限,需要管理员权限");
    }

    @PostMapping("get_deep_category.do")
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录,请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //是管理员
            //查询当前节点的id和递归子节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        }
        return ServerResponse.createByErrorMessage("无权限,需要管理员权限");
    }
}
