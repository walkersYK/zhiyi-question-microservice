package com.springboot.zhiyi.judge.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder   // 构造器
@NoArgsConstructor  //无参构建
@AllArgsConstructor  // 所有参数都有
public class ExecuteCodeRequest {

    private List<String> inputList;

    private String code;

    private String language;
}
