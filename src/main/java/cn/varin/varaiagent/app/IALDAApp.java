package cn.varin.varaiagent.app;


import cn.varin.varaiagent.advisors.MyLogAdvisor;
import cn.varin.varaiagent.chatMemory.FileChatMemory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j

public class IALDAApp {

    public static   final String SYSTEM_PROPERTY = "作为拥有多年学术研究与论文指导经验的专家，精通各学科研究方法与写作规范，需通过开放性问题启发式引导（探研究方向 / 写作瓶颈 / 结构需求等），提供选题评估、文献指导、方法分析、结构建议、表达规范等符合学术规范的专业建议，适配用户基础调整指导深度，以建议而非指令陪伴用户完成论文从选题到定稿全流程，协助解决各类写作问题。";

    private final ChatClient chatClient;
    public IALDAApp(ChatClient.Builder chatClient) {
        this.chatClient = chatClient
                .defaultSystem(SYSTEM_PROPERTY)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(new InMemoryChatMemory()),
                        // 添加自定义advisor
                        new MyLogAdvisor()
                )
                .build();
    }

    public String getMessage(String content,String chatId) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        .param("chat_memory_response_size", 10)).call().chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }




    record IALDAReport(String title, List<String> suggestions){}
    /**
     * 自定义结构化输出
     */
    public IALDAReport getIALDAReport(String content,String chatId) {
        IALDAReport ialdaReport = this.chatClient.prompt()
                .user(content)
                .system(SYSTEM_PROPERTY+"每次对话后都要生成学术分析结果，标题为{用户名}的学术分析报告，内容为建议列表")
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        .param("chat_memory_response_size", 10)).call()
                .entity(IALDAReport.class);

        log.info("IALDAReport:{}", ialdaReport);
        return ialdaReport;
    }


    public String  fileSaveMessage(String content,String chatId) {
        String  fileDir = System.getProperty("user.dir")+"/chat-memory";

        FileChatMemory fileChatMemory = new FileChatMemory(fileDir);

        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        .advisors(new MessageChatMemoryAdvisor(fileChatMemory))
                        .param("chat_memory_response_size", 10))

                .call().chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }

}
