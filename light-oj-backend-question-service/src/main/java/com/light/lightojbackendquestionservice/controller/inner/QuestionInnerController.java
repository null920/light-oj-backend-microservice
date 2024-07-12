package com.light.lightojbackendquestionservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.light.lightojbackendmodel.model.entity.Question;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import com.light.lightojbackendquestionservice.service.QuestionService;
import com.light.lightojbackendquestionservice.service.QuestionSubmitService;
import com.light.lightojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 题目服务内部调用接口
 *
 * @author null&&
 * @Date 2024/7/5 19:46
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    QuestionService questionService;

    @Resource
    QuestionSubmitService questionSubmitService;

    @Override
    @GetMapping("/get/id")
    public Question getQuestionById(@RequestParam("questionId") long questionId) {
        return questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId) {
        return questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit) {
        return questionSubmitService.updateById(questionSubmit);
    }

    @Override
    @PostMapping("/update/accepted_num")
    public boolean updateAcceptedNumById(long questionId) {
        UpdateWrapper<Question> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", questionId);
        updateWrapper.setSql("accepted_num = accepted_num + 1");
        return questionService.update(updateWrapper);
    }
}
