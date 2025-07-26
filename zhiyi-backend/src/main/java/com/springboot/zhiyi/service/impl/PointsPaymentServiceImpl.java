package com.springboot.zhiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.springboot.zhiyi.model.entity.PointsPayment;
import com.springboot.zhiyi.model.enums.PayType;
import com.springboot.zhiyi.service.PointsPaymentService;
import com.springboot.zhiyi.mapper.PointsPaymentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
@Slf4j
public class PointsPaymentServiceImpl extends ServiceImpl<PointsPaymentMapper, PointsPayment>
    implements PointsPaymentService{

    /**
     * 记录支付日志
     * @param plainText
     */
    @Override
    public void createPaymentInfo(String plainText) {

        log.info("记录支付日志");

        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);

        //订单号
        String orderNo = (String)plainTextMap.get("out_trade_no");
        //业务编号
        String transactionId = (String)plainTextMap.get("transaction_id");
        //支付类型
        String tradeType = (String)plainTextMap.get("trade_type");
        //交易状态
        String tradeState = (String)plainTextMap.get("trade_state");
        //用户实际支付金额
        Map<String, Object> amount = (Map)plainTextMap.get("amount");
        Integer payerTotal = ((Double) amount.get("payer_total")).intValue();

        PointsPayment paymentInfo = new PointsPayment();
        paymentInfo.setOrder_no(orderNo);
        paymentInfo.setPay_channel(PayType.WXPAY.getType());
        paymentInfo.setTransaction_id(transactionId);
        paymentInfo.setPay_type(tradeType);
        paymentInfo.setStatus(Byte.valueOf(tradeState));
        paymentInfo.setAmount(payerTotal);

        baseMapper.insert(paymentInfo);
    }

}




