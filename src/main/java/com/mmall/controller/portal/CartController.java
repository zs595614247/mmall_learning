package com.mmall.controller.portal;

import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.ShardRedisPoolUtil;
import com.mmall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    //全选
    @PostMapping("select_all.do")
    public ServerResponse<CartVo> selectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.CHECKED, null);
    }

    //全反选
    @PostMapping("un_select_all.do")
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, null);
    }

    //单独选
    @PostMapping("un_select.do")
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, productId);
    }

    //单独反选
    @PostMapping("select.do")
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, productId);
    }

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那个数量就是10
    @PostMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }


    @PostMapping("list.do")
    public ServerResponse<CartVo> list(HttpServletRequest request) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.list(user.getId());

    }

    @PostMapping("add.do")
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @PostMapping("update.do")
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId) {

        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @PostMapping("delete_product.do")
    public ServerResponse<CartVo> deleteProduct(HttpServletRequest request, String productIds) {
        String loginToken = CookieUtil.readLoginToken(request);
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorMessage("用户未登录无法获得用户信息");
        }
        String userJsonStr = ShardRedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJsonStr, User.class);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }
}
