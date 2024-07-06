package com.light.lightojbackendjudgeservice.service.impl;

import cn.hutool.json.JSONUtil;
import com.light.lightojbackendcommon.common.ErrorCode;
import com.light.lightojbackendcommon.exception.BusinessException;
import com.light.lightojbackendjudgeservice.judge.JudgeManager;
import com.light.lightojbackendjudgeservice.judge.codesandbox.CodeSandbox;
import com.light.lightojbackendjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.light.lightojbackendjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.light.lightojbackendjudgeservice.judge.strategy.JudgeContext;
import com.light.lightojbackendjudgeservice.service.JudgeService;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.light.lightojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.light.lightojbackendmodel.model.codesandbox.JudgeInfo;
import com.light.lightojbackendmodel.model.dto.question.JudgeCase;
import com.light.lightojbackendmodel.model.entity.Question;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import com.light.lightojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.light.lightojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 判题服务实现
 *
 * @author null&&
 * @Date 2024/6/22 18:56
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 根据传入的题目提交 id 获取题目的提交，并且判断题目是否存在
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "判题任务不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 如果状态不为等待中，不能判题
        if (!QuestionSubmitStatusEnum.WAITING.getValue().equals(questionSubmit.getStatus())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中或已结束");
        }
        // 更新题目的提交状态为判题中
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean updateResult = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }

        // 调用代码沙箱，获取到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(executeCodeResponse.getOutputList());
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfoResponse = judgeManager.doJudge(judgeContext);

        // 修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoResponse));
        boolean res = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!res) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }
        return questionFeignClient.getQuestionSubmitById(questionSubmitId);
    }
}
