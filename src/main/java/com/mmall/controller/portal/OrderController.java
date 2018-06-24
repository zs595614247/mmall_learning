package com.mmall.controller.portal;

import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order/")
public class OrderController {


    @Autowired
    private IOrderService iOrderService;

    @RequestMapping("create.do")
    public ServerResponse create(HttpSession session,Integer ShippingId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }

        return null;
    }

}
