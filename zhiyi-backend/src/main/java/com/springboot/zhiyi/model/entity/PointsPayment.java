package com.springboot.zhiyi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 积分支付记录表
 * @TableName points_payment
 */
@TableName(value ="points_payment")
@Data
public class PointsPayment implements Serializable {
    /**
     * 支付记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    @TableField(value = "order_id")
    private Long order_id;

    /**
     * 订单编号
     */
    @TableField(value = "order_no")
    private String order_no;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long user_id;

    /**
     * 支付方式(wxpay/alipay)
     */
    @TableField(value = "pay_type")
    private String pay_type;

    /**
     * 支付金额(分)
     */
    @TableField(value = "amount")
    private Integer amount;

    /**
     * 支付状态(0-待支付,1-支付成功,2-支付失败,3-已关闭)
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 支付渠道
     */
    @TableField(value = "pay_channel")
    private String pay_channel;

    /**
     * 第三方交易号
     */
    @TableField(value = "transaction_id")
    private String transaction_id;

    /**
     * 支付二维码URL
     */
    @TableField(value = "code_url")
    private String code_url;

    /**
     * 支付二维码URL
     */
    @TableField(value = "qr_code_url")
    private String qr_code_url;

    /**
     * 二维码过期时间
     */
    @TableField(value = "qr_code_expire_time")
    private Date qr_code_expire_time;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date pay_time;

    /**
     * 支付过期时间
     */
    @TableField(value = "expire_time")
    private Date expire_time;

    /**
     * 回调通知时间
     */
    @TableField(value = "notify_time")
    private Date notify_time;

    /**
     * 回调内容
     */
    @TableField(value = "notify_result")
    private String notify_result;

    /**
     * 客户端IP
     */
    @TableField(value = "client_ip")
    private String client_ip;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date create_time;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date update_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}