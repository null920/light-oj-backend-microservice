package com.light.lightojbackendjudgeservice.codesandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.light.lightojbackendcommon.common.ErrorCode;
import com.light.lightojbackendcommon.exception.BusinessException;
import com.light.lightojbackendjudgeservice.codesandbox.CodeSandbox;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import org.springframework.beans.factory.annotation.Value;

/**
 * 远程代码沙箱（实际调用接口的代码沙箱）
 *
 * @author null&&
 * @Date 2024/6/21 19:15
 */
public class RemoteCodeSandbox implements CodeSandbox {

    private static final String AUTH_REQUEST_HEADER = "auth";

    private static final String AUTH_REQUEST_SECRET = "null920_secret_key";

    @Value("${codesandbox.remote.url}")
    private String url;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        System.out.println("远程代码沙箱");
        /*
         * 1. 将用户输入的代码、编程语言、输入数据、题目信息封装成 JSON
         * 2. 调用远程接口
         * 3. 获取返回值
         * 4. 将返回值封装成 ExecuteCodeResponse
         * 5. 返回
         */
        String jsonStr = JSONUtil.toJsonStr(executeRequest);
        String secretKey = SecureUtil.sha256(AUTH_REQUEST_SECRET);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, secretKey)
                .body(jsonStr)
                .execute()
                .body();
        if (StrUtil.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error , message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
