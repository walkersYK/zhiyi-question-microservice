package com.springboot.zhiyi.judge;


import com.springboot.zhiyi.judge.codesandbox.model.JudgeInfo;
import com.springboot.zhiyi.judge.strategy.DefaultJudgeStrategy;
import com.springboot.zhiyi.judge.strategy.JavaLanguageJudgeStrategy;
import com.springboot.zhiyi.judge.strategy.JudgeContext;
import com.springboot.zhiyi.judge.strategy.JudgeStrategy;
import com.springboot.zhiyi.model.entity.QuestionSubmit;
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
