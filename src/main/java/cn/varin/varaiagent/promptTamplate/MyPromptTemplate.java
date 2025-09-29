package cn.varin.varaiagent.promptTamplate;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPromptTemplate {

    private String prompt;
    private String name;
    private String major;
    private Map<String,Object > options;

    public MyPromptTemplate() {
    }

    public MyPromptTemplate( String name, String major) {
        this.name = name;
        this.major = major;
        this.prompt = "你好，我是{name}，是{majoy}专业的学生。";
        options = new HashMap<>();
        options.put("name",this.name);
        options.put("majoy",this.major);
    }

    public String getPrompt() {
        PromptTemplate promptTemplate = new PromptTemplate(this.prompt);
        return  promptTemplate.render(this.options);
    }

}
