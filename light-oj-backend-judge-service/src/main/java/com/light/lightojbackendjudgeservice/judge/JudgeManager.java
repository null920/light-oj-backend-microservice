package com.light.lightojbackendjudgeservice.judge;

import com.light.lightojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.light.lightojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.light.lightojbackendjudgeservice.judge.strategy.JudgeContext;
import com.light.lightojbackendjudgeservice.judge.strategy.JudgeStrategy;
import com.light.lightojbackendmodel.model.codesandbox.JudgeInfo;
import com.light.lightojbackendmodel.model.entity.QuestionSubmit;
import com.light.lightojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化判题策略的调用）
 *
 * @author null&&
 * @Date 2024/6/23 17:08
 */
@Service
public class JudgeManager {

    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (QuestionSubmitLanguageEnum.JAVA.getValue().equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
