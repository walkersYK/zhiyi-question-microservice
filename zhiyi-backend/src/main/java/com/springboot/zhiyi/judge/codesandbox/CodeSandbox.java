package com.springboot.zhiyi.judge.codesandbox;


import com.springboot.zhiyi.judge.codesandbox.model.ExecuteCodeRequest;
import com.springboot.zhiyi.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
 * @description 为什么是接口，因为后续方便更换
 */
public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
