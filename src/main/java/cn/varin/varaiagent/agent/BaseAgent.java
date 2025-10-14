package cn.varin.varaiagent.agent;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public abstract class BaseAgent {
    private String name;
    private AgentState agentState=AgentState.IDLE;
    private String SystemPrompt;
    private String nextStepPrompt;
    private Integer maxStep=10;
    private Integer currentStep=0;
    private ChatClient chatClient;
    // 存储提示词的
    private List<Message> contextMessageList= new ArrayList<>();

    /**
     * 步骤
     * @return
     */
    public abstract String step();

    public String run(String userPrompt ){
        System.out.println(currentStep);

        //1. 判断状态是否为空闲
        if (this.agentState!= AgentState.IDLE) {
            throw new RuntimeException("Cannot run agent from state: " + this.agentState);
        }
        //2.判断用户是否输入提示词
        if (StringUtils.isBlank(userPrompt)) {
            throw new RuntimeException("Cannot run agent with empty user prompt");

        }
        // 2.修改状态为运行
        this.agentState =AgentState.RUNNING;
        // 添加上下文
        this.contextMessageList.add(new UserMessage(userPrompt));
        List<String > results = new ArrayList<>();

       try {
           //3. 循环
           while (this.agentState!= AgentState.FINISHED && this.currentStep<=this.maxStep) {
               this.currentStep++;
               StringBuilder result = new StringBuilder();
               result.append("step  " +this.currentStep+":");
               String step = step();
               result.append(step);
               results.add(result.toString());
           }
           if (this.currentStep>this.maxStep) {
               this.agentState =AgentState.FINISHED;
               results.add("终止：已达到最大步数（"+this.maxStep+"+”）");
           }



           return String.join("\n",results);
       }catch (Exception e){
           this.agentState =AgentState.ERROR;
           return "程序执行错误："+e.getMessage();
       }finally {
           // 清除资源
           this.clearup();
       }
    }
    public void clearup(){

    }
}
