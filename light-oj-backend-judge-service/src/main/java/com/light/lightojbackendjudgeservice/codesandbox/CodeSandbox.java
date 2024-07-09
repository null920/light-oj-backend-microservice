package com.light.lightojbackendjudgeservice.codesandbox;


import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 代码沙箱
 *
 * @author null&&
 * @Date 2024/6/21 19:00
 */
public interface CodeSandbox {
    /**
     * 执行代码
     *
     * @param executeRequest
     * @return
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest);
}
