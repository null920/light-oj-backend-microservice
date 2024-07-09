package com.light.lightojbackendjudgeservice.codesandbox.impl;


import com.light.lightojbackendjudgeservice.codesandbox.CodeSandbox;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.light.lightojbackendmodel.model.codesandbox.JudgeInfo;
import com.light.lightojbackendmodel.model.enums.JudgeInfoMessageEnum;
import com.light.lightojbackendmodel.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 *
 * @author null&&
 * @Date 2024/6/21 19:15
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeRequest) {
        List<String> inputList = executeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setTime(100L);
        judgeInfo.setMemory(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
