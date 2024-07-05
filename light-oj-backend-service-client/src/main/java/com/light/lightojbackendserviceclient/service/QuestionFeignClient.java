package com.light.lightojbackendserviceclient.service;

import com.light.lightojbackendmodel.model.entity.Question;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 题目服务
 */
@FeignClient(name = "light-oj-backend-question-service", path = "/api/question/inner")
public interface QuestionFeignClient {

    //    /**
//     * 校验
//     *
//     * @param question
//     * @param add
//     */
//    void validQuestion(Question question, boolean add);
    @GetMapping("/get/id")
    Question getQuestionById(@RequestParam("questionId") long questionId);


    @GetMapping("/question_submit/get/id")
    QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId);

    @PostMapping("/question_submit/update")
    boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit);
}
