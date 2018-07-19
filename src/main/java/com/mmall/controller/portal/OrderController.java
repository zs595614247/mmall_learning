package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Cons;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order/")
public class OrderController {


    @Autowired
    private IOrderService iOrderService;

    //pay

    @PostMapping("query_order_pay_status.do")
    public ServerResponse<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        ServerResponse serverResponse = iOrderService.queryOrderPayStatus(user.getId(), orderNo);
        if (serverResponse.isSuccess()) {
            return ServerResponse.createBySuccess(true);
        }
        return ServerResponse.createBySuccess(false);
    }


    @PostMapping("pay.do")
    public ServerResponse pay(HttpSession session, Long orderNo, HttpServletRequest request) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        //二维码上传路径
        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(user.getId(), orderNo, path);
    }

    @PostMapping("alipay_callback.do")
    public Object alipayCallback(HttpServletRequest request) {
        log.info("支付宝回调");
        Map<String,String> params = Maps.newHashMap();
        Map<String, String[]> requestParameterMap = request.getParameterMap();
        for (String name:requestParameterMap.keySet()) {
            String[] values = requestParameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1)?valueStr+values[i]:valueStr+values[i]+",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //验证回调的正确性,并且避免重复通知
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if (!alipayRSACheckedV2) {
                return ServerResponse.createByErrorMessage("非法请求验证不通过");
            }
        } catch (AlipayApiException e) {
            log.error("支付宝验证回调异常",e);
        }
        ServerResponse response = iOrderService.alipayCallback(params);
        if (response.isSuccess()) {
            return Cons.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Cons.AlipayCallback.RESPONSE_FAILED;
    }
    //order

    @PostMapping("create.do")
    public ServerResponse create(HttpSession session,Integer shippingId) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iOrderService.createOrder(user.getId(),shippingId);
    }

    @PostMapping("cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iOrderService.cancel(user.getId(), orderNo);
    }

    @PostMapping("get_order_cart_product.do")
    public ServerResponse getOrderCartProduct(HttpSession session) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iOrderService.getOrderCartProduct(user.getId());
    }

    @PostMapping("deatil.do")
    public ServerResponse deatil(HttpSession session,Long orderNo) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iOrderService.getOrderDetail(user.getId(),orderNo);
    }

    @PostMapping("list.do")
    public ServerResponse deatil(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,  @RequestParam(value = "pageSize",defaultValue = "10") int pageSize) {
        User user = (User) session.getAttribute(Cons.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDeac());
        }
        return iOrderService.getOrderList(user.getId(), pageNum, pageSize);
    }
}
