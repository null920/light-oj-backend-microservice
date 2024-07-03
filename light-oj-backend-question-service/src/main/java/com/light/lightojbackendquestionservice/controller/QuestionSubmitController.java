package com.light.lightojbackendquestionservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.lightojbackendcommon.common.BaseResponse;
import com.light.lightojbackendcommon.common.ErrorCode;
import com.light.lightojbackendcommon.common.ResultUtils;
import com.light.lightojbackendcommon.exception.BusinessException;
import com.light.lightojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.light.lightojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import com.light.lightojbackendmodel.model.entity.User;
import com.light.lightojbackendmodel.model.vo.QuestionSubmitVO;
import com.light.lightojbackendquestionservice.service.QuestionSubmitService;
import com.light.lightojbackendserviceclient.service.JudgeService;
import com.light.lightojbackendserviceclient.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest 请求封装类
     * @param request                  请求
     * @return 题目提交 id
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        // 执行判题服务
        CompletableFuture.runAsync(() -> judgeService.doJudge(questionSubmitId));
        return ResultUtils.success(questionSubmitId);
    }


    /**
     * 分页获取题目提交列表（除了管理员，普通用户只能看到公开信息）
     *
     * @param questionSubmitQueryRequest 请求封装类
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        final User loginUser = userService.getLoginUser(request);
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, request));
    }

}
