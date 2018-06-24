package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.Cons;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.OrderMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Order;
import com.mmall.pojo.OrderItem;
import com.mmall.pojo.Product;
import com.mmall.service.IOrderService;
import com.mmall.util.BigDecimalUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public ServerResponse craeteOrder(Integer userId, Integer shippingId) {
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        ServerResponse<List<OrderItem>> response = getCartOrderItem(userId, cartList);
        if (!response.isSuccess()) {
            return response;
        }
        List<OrderItem> orderItemList = response.getData();
        BigDecimal bigDecimal = getOrderTotalPrice(orderItemList);

        //生成订单
        Order order = assembleOrder(userId, shippingId, bigDecimal);


        return null;
    }

    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment) {
        Order order = new Order();
        long orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        order.setStatus(Cons.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Cons.PaymentTypeEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        int rowCount = orderMapper.insert(order);
        if (rowCount < 0) {
            return null;
        }
        return order;
    }

    private long generateOrderNo() {
        long orderNo = System.currentTimeMillis();
        return orderNo + new Random().nextInt(100);
    }

    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
        BigDecimal bigDecimal = new BigDecimal("0");
        for (OrderItem orderItem :
                orderItemList) {
            bigDecimal = BigDecimalUtil.add(bigDecimal.doubleValue(), orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }

    private ServerResponse<List<OrderItem>> getCartOrderItem(Integer userId, List<Cart> cartList) {
        List<OrderItem> orderItemList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(orderItemList)) {
            return ServerResponse.createByErrorMessage("购物车为空");
        }

        //校验购物车中的数据,包括产品的状态和数量
        for (Cart cartItem :
                cartList) {
            OrderItem orderItem = new OrderItem();
            Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
            if (Cons.ProductStatusEnum.ON_SALE.getCode() == product.getStatus()) {
                return ServerResponse.createByErrorMessage("产品不在售卖状态");
            }
            if (cartItem.getQuantity() > product.getStock()) {
                return ServerResponse.createByErrorMessage("库存不足");
            }
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cartItem.getQuantity()));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createBySuccess(orderItemList);
    }
}
