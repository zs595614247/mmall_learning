package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface IOrderService {

    ServerResponse craeteOrder(Integer userId, Integer shippingId);
}
