package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/shipping/")
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;

    @GetMapping("add.do")
    public ServerResponse<Map> add(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iShippingService.add(user.getId(), shipping);
    }

    @GetMapping("del.do")
    public ServerResponse del(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iShippingService.del(user.getId(), shippingId);
    }

    @GetMapping("update.do")
    public ServerResponse update(HttpSession session, Shipping shipping) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iShippingService.update(user.getId(), shipping);
    }

    @GetMapping("select.do")
    public ServerResponse<Shipping> select(HttpSession session, Integer shippingId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iShippingService.select(user.getId(), shippingId);
    }

    @GetMapping("list.do")
    public ServerResponse<PageInfo> list(HttpSession session,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iShippingService.list(user.getId(), pageNum, pageSize);
    }
}
