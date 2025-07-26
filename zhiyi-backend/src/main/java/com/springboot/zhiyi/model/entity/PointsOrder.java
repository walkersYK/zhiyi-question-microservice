package com.springboot.zhiyi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 积分订单表
 * @TableName points_order
 */
@TableName(value ="points_order")
@Data
public class PointsOrder implements Serializable {
    /**
     * 订单ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 商品ID
     */
    @TableField(value = "product_id")
    private Long product_id;

    /**
     * 积分数量
     */
    @TableField(value = "points")
    private Integer points;

    /**
     * 订单金额(分)
     */
    @TableField(value = "amount")
    private Integer amount;

    /**
     * 实付金额(分)
     */
    @TableField(value = "actual_amount")
    private Integer actual_amount;

    /**
     * 支付方式(wxpay/alipay)
     */
    @TableField(value = "pay_type")
    private String pay_type;

    /**
     * 订单状态(0-待支付,1-已支付,2-已取消,3-已退款)
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 支付时间
     */
    @TableField(value = "pay_time")
    private Date pay_time;

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
     * 客户端IP
     */
    @TableField(value = "client_ip")
    private String client_ip;

    /**
     * 设备信息
     */
    @TableField(value = "device_info")
    private String device_info;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

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
     * 二维码原始内容(加密存储)
     */
    @TableField(value = "qr_code_content")
    private String qr_code_content;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}