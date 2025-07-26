package com.springboot.zhiyi.service;

import com.springboot.zhiyi.model.entity.PointsPayment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface PointsPaymentService extends IService<PointsPayment> {

    void createPaymentInfo(String plainText);
}
