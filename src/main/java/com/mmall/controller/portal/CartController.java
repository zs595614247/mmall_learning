package com.mmall.controller.portal;

import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private ICartService iCartService;

    //全选
    @PostMapping("select_all.do")
    public ServerResponse<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.CHECKED, null);
    }

    //全反选
    @PostMapping("un_select_all.do")
    public ServerResponse<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, null);
    }

    //单独选
    @PostMapping("un_select.do")
    public ServerResponse<CartVo> unSelect(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, productId);
    }

    //单独反选
    @PostMapping("select.do")
    public ServerResponse<CartVo> select(HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.selectOrUnSelect(user.getId(), Cons.Cart.UN_CHECKED, productId);
    }

    //查询当前用户的购物车里面的产品数量,如果一个产品有10个,那个数量就是10
    @PostMapping("get_cart_product_count.do")
    public ServerResponse<Integer> getCartProductCount(HttpSession session) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createBySuccess(0);
        }
        return iCartService.getCartProductCount(user.getId());
    }


    @PostMapping("list.do")
    public ServerResponse<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.list(user.getId());

    }

    @PostMapping("add.do")
    public ServerResponse<CartVo> add(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.add(user.getId(), productId, count);
    }

    @PostMapping("update.do")
    public ServerResponse<CartVo> update(HttpSession session, Integer count, Integer productId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.update(user.getId(), productId, count);
    }

    @PostMapping("delete_product.do")
    public ServerResponse<CartVo> deleteProduct(HttpSession session, String productIds) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iCartService.deleteProduct(user.getId(), productIds);
    }
}
