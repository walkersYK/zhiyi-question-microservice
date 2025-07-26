package com.springboot.zhiyi.controller;

import com.google.gson.Gson;
import com.springboot.zhiyi.common.BaseResponse;
import com.springboot.zhiyi.common.ResultUtils;
import com.springboot.zhiyi.service.WxPayService;
import com.springboot.zhiyi.utils.HttpUtils;
import com.springboot.zhiyi.utils.WechatPay2ValidatorForRequest;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YunJin
 * @version 1.0
 * @description: TODO
 * @date 2025/6/13 14:37
 */
@RestController
@RequestMapping("/api/wx-pay")
@Slf4j
// swagger 注解
@Api("微信支付")
public class WxPayController {
    @Resource
    private WxPayService wxPayService;

    @Resource
    private Verifier verifier;


    @ApiOperation("生成支付二维码")
    @PostMapping("native/{productId}")
    public BaseResponse<?> nativePay(@PathVariable Long productId) throws Exception {
        log.info("支付请求");

        // 返回支付二维码连接和订单号
        Map<String, Object> stringObjectMap = wxPayService.nativePay(productId);
        return ResultUtils.success(stringObjectMap);
    }

    /**
     * 支付通知
     * 微信支付通过支付通知接口将用户支付成功消息通知给商户
     */
    @ApiOperation("支付通知")
    @PostMapping("/native/notify")
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response){

        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();//应答对象

        try {

            //处理通知参数
            String body = HttpUtils.readData(request);
            Map<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String)bodyMap.get("id");
            log.info("支付通知的id ===> {}", requestId);
            //log.info("支付通知的完整数据 ===> {}", body);
            //int a = 9 / 0;

            //签名的验证
            WechatPay2ValidatorForRequest wechatPay2ValidatorForRequest
                    = new WechatPay2ValidatorForRequest(verifier, requestId, body);
            if(!wechatPay2ValidatorForRequest.validate(request)){

                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }
            log.info("通知验签成功");

            //处理订单
            wxPayService.processOrder(bodyMap);

            //应答超时
            //模拟接收微信端的重复通知
            TimeUnit.SECONDS.sleep(5);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }

    }

}
