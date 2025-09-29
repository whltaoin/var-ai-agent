package cn.varin.varaiagent.test;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;
// 通‌过建造者模式手动构造

@Component

@Slf4j
// public class ChatClientTest  implements CommandLineRunner {
public class ChatClientTest  {
    String uuid = UUID.randomUUID().toString();
    Integer storageNum=10;
    @Resource
    private ChatModel dashScopeChatModel;



    public void run(String... args) throws Exception {

        log.info("uuid:{}",uuid);


        // 使用builder方式
        ChatClient chatClient = getChatClient();
        // 多轮调用
        ChatResponse chatResponse = getChatResponse(chatClient,"你好，我叫varin");

        System.out.println(chatResponse.getResult().getOutput().getText());
        System.out.println("===================");
        ChatResponse chatResponse1 = getChatResponse(chatClient,"我是软件工程专业的学生");
        System.out.println(chatResponse1.getResult().getOutput().getText());
        System.out.println("===================");

        ChatResponse chatResponse2 = getChatResponse(chatClient,"我叫什么？");
        System.out.println(chatResponse2.getResult().getOutput().getText());
        System.out.println("===================");



    }
    public ChatResponse getChatResponse(ChatClient chatClient,String content) {
       return chatClient.prompt()
                .system(system
                        ->system.param("name","学术专家"))
                .advisors(advisor -> advisor.param("chat_memory_conversation_id",uuid)
                        .param("chat_memory_response_size", storageNum))

                .user(content).call().chatResponse();
    }
    private ChatMemory chatMemory= new InMemoryChatMemory();


    public ChatClient getChatClient() {
       return  ChatClient.builder(dashScopeChatModel)
               .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .defaultSystem("你是{name}")

                        .
                build();

    }


}
