package com.springboot.zhiyi.model.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author YunJin
 * @version 1.0
 * @description: TODO
 * @date 2025/6/13 14:04
 */
@Data
public class WxPayVO {

    private Integer code;

    private String msg;

    private Map<String,Object> data =new HashMap<>();
}
