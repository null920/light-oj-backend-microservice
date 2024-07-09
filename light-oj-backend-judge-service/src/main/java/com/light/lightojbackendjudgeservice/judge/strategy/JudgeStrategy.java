package com.light.lightojbackendjudgeservice.judge.strategy;


import com.light.lightojbackendmodel.model.codesandbox.JudgeInfo;

/**
 * 判题策略
 *
 * @author null&&
 * @Date 2024/6/23 16:05
 */
public interface JudgeStrategy {
    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return 判题信息
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
