package com.springboot.zhiyi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 积分商品表
 * @TableName points_product
 */
@TableName(value ="points_product")
@Data
public class PointsProduct implements Serializable {
    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 积分数量
     */
    @TableField(value = "points")
    private Integer points;

    /**
     * 价格(分)
     */
    @TableField(value = "price")
    private Integer price;

    /**
     * 原价(分)
     */
    @TableField(value = "original_price")
    private Integer original_price;

    /**
     * 优惠金额(分)
     */
    @TableField(value = "discount")
    private Integer discount;

    /**
     * 标签(热销/推荐等)
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 标签颜色
     */
    @TableField(value = "tag_color")
    private String tag_color;

    /**
     * 状态(0-下架,1-上架)
     */
    @TableField(value = "status")
    private Byte status;

    /**
     * 排序权重
     */
    @TableField(value = "sort_order")
    private Integer sort_order;

    /**
     * 商品描述
     */
    @TableField(value = "description")
    private String description;

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