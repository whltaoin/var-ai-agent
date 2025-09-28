package cn.varin.varaiagent.serivce;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest

public class ChatServiceTest {

    @Resource
    private ChatService chatService;
    @Test
    public void run()  {
        ChatResponse chatResponse = chatService.sendMessage("你好,我是一名软件工程专业的学生");
        System.out.println(chatResponse.getResult().getOutput().getText());
    }
}
