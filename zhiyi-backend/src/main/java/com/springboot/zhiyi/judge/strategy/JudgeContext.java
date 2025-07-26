package com.springboot.zhiyi.judge.strategy;


import com.springboot.zhiyi.judge.codesandbox.model.JudgeInfo;
import com.springboot.zhiyi.model.dto.question.JudgeCase;
import com.springboot.zhiyi.model.entity.Question;
import com.springboot.zhiyi.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文（用于定义在策略中传递的参数）
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
