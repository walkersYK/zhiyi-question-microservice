package com.springboot.zhiyi.controller;

import com.springboot.zhiyi.common.BaseResponse;
import com.springboot.zhiyi.common.ResultUtils;
import com.springboot.zhiyi.model.entity.PointsOrder;
import com.springboot.zhiyi.model.entity.User;
import com.springboot.zhiyi.service.PointsOrderService;
import com.springboot.zhiyi.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author YunJin
 * @version 1.0
 * @description: TODO
 * @date 2025/7/17 16:46
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "客户订单相关接口")
public class OrderController {
    @Autowired
    private PointsOrderService pointsOrderService;

    @Autowired
    private UserService userService;

    @ApiOperation("创建订单")
    @PostMapping("/submit")
    public BaseResponse<PointsOrder> submit(@RequestBody PointsOrder pointsOrder, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PointsOrder order = new PointsOrder();
        order.setUser_id(loginUser.getId());
        BeanUtils.copyProperties(pointsOrder, order);
        pointsOrderService.submit(pointsOrder);
        return ResultUtils.success(null);
    }
}
