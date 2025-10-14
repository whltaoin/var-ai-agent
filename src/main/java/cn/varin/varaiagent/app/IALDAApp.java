package cn.varin.varaiagent.app;


import cn.varin.varaiagent.advisors.MyLogAdvisor;
import cn.varin.varaiagent.chatMemory.FileChatMemory;
import cn.varin.varaiagent.config.IALDAAppRagAlibabaAdvisorConfig;
import cn.varin.varaiagent.config.ToolRegisterConfig;
import cn.varin.varaiagent.rag.factory.AppRagAdvisorFactory;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrievalAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 读取本地知识库
     */
    @Resource
    private VectorStore iALDAAppVectorStore;
    public String  localhostVectorSaveMessage(String content,String chatId) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        // 自定义日志

                        .param("chat_memory_response_size", 10))
                .advisors(new MyLogAdvisor())
                // 本地知识库
                .advisors(new QuestionAnswerAdvisor(iALDAAppVectorStore)).call().chatResponse();

        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }

    /**
     * 读取阿里云知识库
     */
    @Resource
    private Advisor iALDAAppRagAlibabaAdvisor;

    public String  cloudAlibabaDoChatWithRag(String content,String chatId) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        // 自定义日志

                        .param("chat_memory_response_size", 10))
                .advisors(new MyLogAdvisor())
                // 本地知识库
                .advisors(iALDAAppRagAlibabaAdvisor).call().chatResponse();

        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }


    /**
     * 读取本地知识库+状态过滤
     */

    public String  localhostVectorSaveMessageByFilterStatus(String content,String chatId,String status) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        // 自定义日志

                        .param("chat_memory_response_size", 10))
                .advisors(
                        AppRagAdvisorFactory.createLoveAppRagCustomAdvisor(iALDAAppVectorStore,status)
                )
                // 本地知识库
                .advisors(new QuestionAnswerAdvisor(iALDAAppVectorStore)).call().chatResponse();

        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }

    @Resource
    private ToolCallback[] allTools;

    /**
     * Ai调用工具
     * @param content
     * @param chatId
     * @return
     */

    public String getMessagewithTools(String content,String chatId) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .tools(allTools)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        .param("chat_memory_response_size", 10)).call().chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }
    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    /**
     * 客户端调用MCP服务
     * @param content
     * @param chatId
     * @return
     */

    public String getMessagewithMCPClient(String content,String chatId) {
        ChatResponse chatResponse = this.chatClient.prompt()
                .user(content)
                .tools(toolCallbackProvider)
                .advisors(advisor -> advisor.param("chat_memory_conversation_id", chatId)
                        .param("chat_memory_response_size", 10)).call().chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        log.info("text:{}", text);
        return text;
    }






}
