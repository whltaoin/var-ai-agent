package cn.varin.varaiagent.invoke;

import cn.varin.varaiagent.config.AliyunConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;

public class Langchaiin4jInvoke {
    public static void main(String[] args) {

        ChatModel build = QwenChatModel.builder().apiKey(AliyunConfig.accessKeyId)
                .modelName("qwen-plus")
                .build();
        String helloWorld = build.chat("hello world");
        System.out.println(helloWorld);
    }
}
