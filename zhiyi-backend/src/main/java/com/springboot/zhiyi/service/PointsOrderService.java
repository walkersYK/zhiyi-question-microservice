package com.springboot.zhiyi.service;

import com.springboot.zhiyi.model.entity.PointsOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springboot.zhiyi.model.enums.OrderStatus;

/**
 *
 */
public interface PointsOrderService extends IService<PointsOrder> {

     PointsOrder createOrderByProductId(Long productId);

    void saveCodeUrl(String orderNo, String codeUrl);

    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);

    Byte getOrderStatus(String orderNo);

    /**
     * 创建订单
     * @param pointsOrder
     */
    void submit(PointsOrder pointsOrder);
}
