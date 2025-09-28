package cn.varin.varaiagent.serivce;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;
// 构造期创建
@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatClient.Builder builder) {
        this.chatClient = builder
            .defaultSystem("你是学术专家")
            .build();
    }

    public ChatResponse sendMessage(String content) {
       return  this.chatClient.prompt()
                .user(content)
                .call()
                .chatResponse();

    }
}