package com.light.lightojbackendjudgeservice.codesandbox.impl;


import com.light.lightojbackendjudgeservice.codesandbox.CodeSandbox;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeResponse;

/**
 * 第三方代码沙箱（调用网上现成的代码沙箱）
 *
 * @author null&&
 * @Date 2024/6/21 19:15
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
