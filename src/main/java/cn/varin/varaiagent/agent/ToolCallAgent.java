package cn.varin.varaiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class ToolCallAgent extends ReActAgent{
    // 可用用具
    private ToolCallback[] userableTools;
    // 每次模型回答的响应
    private ChatResponse tollCallchatResponse;
    // 工具管理
    private  final ToolCallingManager toolCallingManager;
    //自定义模型参数
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] userableTools) {
        super();
        this.userableTools = userableTools;

        this.toolCallingManager= ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                // 关闭模型托管工具功能，现在需要我们自主实现
                .withProxyToolCalls(true)
                .build();

    }

    @Override
    public Boolean think() {

        // 判断下一步骤的提示词是否不为空
        if (getNextStepPrompt() !=null && StringUtils.isNotBlank(getNextStepPrompt())) {
            // 有值的话添加到上下文
            getContextMessageList().add(new UserMessage(getNextStepPrompt()));
        }
        List<Message> contextMessageList = getContextMessageList();
        // 构建prompt
        Prompt prompt = new Prompt(contextMessageList, this.chatOptions);
       try {
           // 开始交互
           ChatResponse chatResponse = getChatClient().prompt(prompt)
                   .system(getSystemPrompt())

                   .tools(this.userableTools)
                   .call()
                   .chatResponse();
           this.tollCallchatResponse = chatResponse;
           //   得到响应流
           AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
           // 得到思考内容
           String result = assistantMessage.getText();
           //获取模型调用了哪些工具
           List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();
           // 打印日志
           log.info(getName()+"的思考内容为："+result);
           log.info(getName() + "选择了 " + toolCalls.size() + " 个工具来使用");


           String toolCallInfo = toolCalls.stream()
                   .map(toolCall -> String.format("工具名称：%s，参数：%s",
                           toolCall.name(),
                           toolCall.arguments())
                   )
                   .collect(Collectors.joining("\n"));
           log.info(toolCallInfo);

           if (toolCalls.isEmpty()) {

               //没有工具调用
               getContextMessageList().add(assistantMessage);
               return false;
           }else {
               return true;
           }
       }catch (Exception e){
           log.error(getName() + "的思考过程遇到了问题: " + e.getMessage());
           getContextMessageList().add(
                   new AssistantMessage("处理时遇到错误: " + e.getMessage()));
           return false;
       }


    }

    @Override
    public String act() {

        // 1 判断ai思考时模型是否需要工具
        if (!this.getTollCallchatResponse().hasToolCalls()) {
            return "没有工具调用";

        }
        // 2. 构建提示词
        Prompt prompt = new Prompt(getContextMessageList(), this.chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, this.getTollCallchatResponse());

        // 因为ToolExecutionResult.conversationHistory中存储了所有的上下文信息，所以我们替换下，保持一致
        setContextMessageList(toolExecutionResult.conversationHistory());
        //获取最后一条信息
        ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());

        String results = toolResponseMessage.getResponses().stream()
                .map(response -> "工具 " + response.name() + " 完成了它的任务！结果: " + response.responseData())
                .collect(Collectors.joining("\n"));
        log.info(results);

        // 判断是否调用了终止工具

        boolean doTerminateisExec = toolResponseMessage.getResponses().stream()
                .anyMatch(response -> "doTerminate".equals(response.name()));

        // 如果调用了直接结束
        if (doTerminateisExec) {

            setAgentState(AgentState.FINISHED);
        }

        return results;
    }
}
