package com.zhiyi.zhiyijudgeservice.judge;

import com. zhiyi.zhiyijudgeservice.judge.strategy.DefaultJudgeStrategy;
import com. zhiyi.zhiyijudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com. zhiyi.zhiyijudgeservice.judge.strategy.JudgeContext;
import com. zhiyi.zhiyijudgeservice.judge.strategy.JudgeStrategy;
import com. zhiyi.zhiyimodel.model.codesandbox.JudgeInfo;
import com. zhiyi.zhiyimodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
