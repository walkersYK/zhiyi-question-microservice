package com.springboot.zhiyi.judge.codesandbox;

import com.springboot.zhiyi.judge.codesandbox.impl.ExampleCodeSandbox;
import com.springboot.zhiyi.judge.codesandbox.model.ExecuteCodeRequest;
import com.springboot.zhiyi.judge.codesandbox.model.ExecuteCodeResponse;
import com.springboot.zhiyi.model.entity.Question;
import com.springboot.zhiyi.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandboxTest {

    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void executeCode() {
        CodeSandbox codeSandbox = new ExampleCodeSandbox();
        String code = "int main() {\n";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String > inputlist = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputlist)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
        System.out.println("执行消息: " + executeCodeResponse.getMessage());
    }

    @Test
    void executecodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String code = "int main(){ }";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executecodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executecodeResponse);
        System.out.println("执行消息: " + executecodeResponse.getMessage());
    }
}