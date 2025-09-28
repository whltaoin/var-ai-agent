package cn.varin.varaiagent.invoke;

import dev.langchain4j.agent.tool.P;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringAiInvoke  implements CommandLineRunner {


//    @Resource
//    private ChatModel dashScopeChatModel;


    @Override
    public void run(String... args) throws Exception {

//        AssistantMessage hello = dashScopeChatModel.call(new Prompt("解释AI一词"))
//                .getResult()
//                .getOutput();
//        System.out.println(hello.getText());
//        ChatResponse call = dashScopeChatModel.call(new Prompt(hello.getText()));

    }
}
