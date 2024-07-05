package com.light.lightojbackendjudgeservice.controller.inner;

import com.light.lightojbackendjudgeservice.judge.JudgeService;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import com.light.lightojbackendserviceclient.service.JudgeFeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 判题服务内部调用接口
 *
 * @author null&&
 * @Date 2024/7/5 19:58
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    JudgeService judgeService;

    /**
     * 判题
     *
     * @param questionSubmitId 题目提交id
     * @return 判题信息
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId) {
        return judgeService.doJudge(questionSubmitId);
    }
}
