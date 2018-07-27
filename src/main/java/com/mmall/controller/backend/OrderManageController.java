package com.mmall.controller.backend;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.service.IOrderService;
import com.mmall.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/order/")
public class OrderManageController {

    private final IOrderService iOrderService;

    @Autowired
    public OrderManageController(IOrderService iOrderService) {
        this.iOrderService = iOrderService;
    }

    @RequestMapping(value = "list.do")
    public ServerResponse<PageInfo> orderList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iOrderService.manageList(pageNum, pageSize);
    }

    @RequestMapping("detail.do")
    public ServerResponse<OrderVo> detail(HttpServletRequest request, Long orderNo) {
        return iOrderService.manageDetail(orderNo);
    }

    @RequestMapping("search.do")
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1") int pageNum, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return iOrderService.manageSearch(orderNo, pageNum, pageSize);
    }

    @RequestMapping("send_goods.do")
    public ServerResponse<String> orderSendGoods(HttpServletRequest request, Long orderNo) {
        return iOrderService.manageSendGoods(orderNo);
    }
}
