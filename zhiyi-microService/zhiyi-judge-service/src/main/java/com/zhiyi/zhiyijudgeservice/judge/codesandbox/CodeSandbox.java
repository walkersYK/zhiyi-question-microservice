package com.zhiyi.zhiyijudgeservice.judge.codesandbox;

import com. zhiyi.zhiyimodel.model.codesandbox.ExecuteCodeRequest;
import com. zhiyi.zhiyimodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱接口定义
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
