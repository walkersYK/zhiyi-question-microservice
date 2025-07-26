package com.springboot.zhiyi.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.*;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

@Configuration
@PropertySource(value = "classpath:wxpay.properties", ignoreResourceNotFound = false)
@ConfigurationProperties(prefix = "wx.pay")
@Data
@Slf4j
public class WxPayConfig {

    private String mchId;
    private String mchSerialNo;
    private String privateKeyPath;
    private String apiV3Key;
    private String appid;
    private String domain;
    private String notifyDomain;
    private String partnerKey;

    @PostConstruct
    public void checkConfig() {
        if (mchId == null || mchSerialNo == null || privateKeyPath == null || apiV3Key == null) {
            throw new IllegalStateException("微信支付配置不完整，请检查wxpay.properties文件");
        }
        log.info("微信支付配置加载成功，商户号: {}", mchId);
    }

    public PrivateKey getPrivateKey(String filename) {
        try {
            if (filename.startsWith("classpath:")) {
                InputStream is = getClass().getResourceAsStream(filename.replace("classpath:", ""));
                if (is == null) {
                    throw new FileNotFoundException("类路径下未找到私钥文件: " + filename);
                }
                return PemUtil.loadPrivateKey(is);
            } else {
                return PemUtil.loadPrivateKey(new FileInputStream(filename));
            }
        } catch (Exception e) {
            throw new RuntimeException("加载私钥失败: " + filename, e);
        }
    }

    @Bean
    public ScheduledUpdateCertificatesVerifier getVerifier() {
        log.info("初始化微信支付签名验证器...");
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        PrivateKeySigner privateKeySigner = new PrivateKeySigner(mchSerialNo, privateKey);
        WechatPay2Credentials credentials = new WechatPay2Credentials(mchId, privateKeySigner);

        return new ScheduledUpdateCertificatesVerifier(
                credentials,
                apiV3Key.getBytes(StandardCharsets.UTF_8));
    }

    @Bean(name = "wxPayClient")
    public CloseableHttpClient getWxPayClient(ScheduledUpdateCertificatesVerifier verifier) {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        return WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier))
                .build();
    }

    @Bean(name = "wxPayNoSignClient")
    public CloseableHttpClient getWxPayNoSignClient() {
        PrivateKey privateKey = getPrivateKey(privateKeyPath);
        return WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator((response) -> true)
                .build();
    }
}