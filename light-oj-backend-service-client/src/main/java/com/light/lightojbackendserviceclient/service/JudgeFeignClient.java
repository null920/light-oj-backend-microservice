package com.light.lightojbackendserviceclient.service;

import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 *
 * @author null&&
 * @Date 2024/6/22 18:49
 */
@FeignClient(name = "light-oj-backend-judge-service", path = "/api/judge")
public interface JudgeFeignClient {

    /**
     * 判题
     *
     * @param questionSubmitId 题目提交id
     * @return
     */
    @PostMapping("/do")
    QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
