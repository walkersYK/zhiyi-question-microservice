package com.springboot.zhiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springboot.zhiyi.common.Orders;
import com.springboot.zhiyi.mapper.PointsProductMapper;
import com.springboot.zhiyi.mapper.UserMapper;
import com.springboot.zhiyi.model.entity.PointsOrder;
import com.springboot.zhiyi.model.entity.PointsProduct;
import com.springboot.zhiyi.model.enums.OrderStatus;
import com.springboot.zhiyi.service.PointsOrderService;
import com.springboot.zhiyi.mapper.PointsOrderMapper;
import com.springboot.zhiyi.utils.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
@Slf4j
public class PointsOrderServiceImpl extends ServiceImpl<PointsOrderMapper, PointsOrder>
    implements PointsOrderService{
    @Resource
    private PointsProductMapper pointsProductMapper;

    @Override
    public PointsOrder createOrderByProductId(Long productId) {

        //查找已存在但未支付的订单
        PointsOrder pointsOrder1 = this.getNoPayOrderByProductId(productId);
        if(pointsOrder1==null){
            return pointsOrder1;
        }
        PointsProduct pointsProduct = pointsProductMapper.selectById(productId);

        //生成订单
        PointsOrder pointsOrder = new PointsOrder();
        pointsOrder.setPoints(pointsProduct.getPoints());
        pointsOrder.setOrder_no(OrderNoUtils.getOrderNo());
        pointsOrder.setProduct_id(pointsProduct.getId());
        pointsOrder.setAmount(pointsProduct.getPrice());
        pointsOrder.setStatus(pointsProduct.getStatus());

        // 存入数据库
        baseMapper.insert(pointsOrder);

        return pointsOrder;
    }

    /**
     * 存储订单二维码
     * @param orderNo
     * @param codeUrl
     */
    @Override
    public void saveCodeUrl(String orderNo, String codeUrl) {

        QueryWrapper<PointsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        PointsOrder orderInfo = new PointsOrder();
        orderInfo.setQr_code_url(codeUrl);

        baseMapper.update(orderInfo, queryWrapper);
    }

    /**
     * 根据订单号更新订单状态
     * @param orderNo
     * @param orderStatus
     */
    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {

        log.info("更新订单状态 ===> {}", orderStatus.getType());

        QueryWrapper<PointsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        PointsOrder orderInfo = new PointsOrder();
        orderInfo.setStatus(Byte.valueOf(orderStatus.getType()));

        baseMapper.update(orderInfo, queryWrapper);
    }


    /**
     * 根据订单号获取订单状态
     *
     * @param orderNo
     * @return
     */
    @Override
    public Byte getOrderStatus(String orderNo) {

        QueryWrapper<PointsOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        PointsOrder orderInfo = baseMapper.selectOne(queryWrapper);
        if(orderInfo == null){
            return null;
        }
        return orderInfo.getStatus();
    }

    @Override
    public void submit(PointsOrder pointsOrder) {
        //1、保存订单数据
        PointsOrder order = new PointsOrder();
        BeanUtils.copyProperties(pointsOrder, order);
        order.setStatus(Orders.PENDING_PAYMENT);
        //2、保存明细数据
        //3、清空购物车
    }


    private PointsOrder getNoPayOrderByProductId(Long productId) {
        QueryWrapper<PointsOrder> queryWrapper = new QueryWrapper<PointsOrder>()
                .eq("product_id", productId)
                .eq("status", OrderStatus.NOTPAY.getType());
        PointsOrder pointsOrder = baseMapper.selectOne(queryWrapper);
        return pointsOrder;
    }
}




