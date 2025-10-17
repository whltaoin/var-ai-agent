package cn.varin.varaiagent.agent;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

    /**
     * 同步调用
     * @param userPrompt
     * @return
     */
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


    /**
     * 异步流式调用
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt ){
        SseEmitter sseEmitter = new SseEmitter(300000L);

        CompletableFuture.runAsync(() -> {


            try {
                //1. 判断状态是否为空闲
                if (this.agentState!= AgentState.IDLE) {
                    sseEmitter.send("Error:无法从状态运行代理："+this.agentState);
                    sseEmitter.complete();
                    return ;


                }
                //2.判断用户是否输入提示词
                if (StringUtils.isBlank(userPrompt)) {
                    sseEmitter.send("Error:Prompt不能为空");
                    sseEmitter.complete();
                    return ;


                }
                // 2.修改状态为运行
                this.agentState =AgentState.RUNNING;
                // 添加上下文
                this.contextMessageList.add(new UserMessage(userPrompt));
                try {

                    //3. 循环
                    while (this.agentState!= AgentState.FINISHED && this.currentStep<=this.maxStep) {
                        this.currentStep++;
                        StringBuilder result = new StringBuilder();

                        String step = step();
                        result.append("step  " +this.currentStep+":").append(step);
                        sseEmitter.send(result.toString());
                    }
                    if (this.currentStep>this.maxStep) {
                        this.agentState =AgentState.FINISHED;
                        sseEmitter.send("终止：已达到最大步数（"+this.maxStep+"+”）");
                    }



                    sseEmitter.complete();

                }catch (Exception e){
                    this.agentState =AgentState.ERROR;
                    try {
                        sseEmitter.send( "程序执行错误："+e.getMessage());
                        sseEmitter.complete();
                    }catch (Exception e1){
                        sseEmitter.completeWithError(e1) ;
                    }
                }finally {
                    this.clearup();
                }

            }catch (Exception e){
                sseEmitter.completeWithError(e);
            }


        });
        // 处理sseEmitter对象的超时和完成时间
        sseEmitter.onTimeout(()->{
            this.agentState =AgentState.ERROR;
            this.clearup();
            log.warn("SSE connection Timed out");
        });

        sseEmitter.onCompletion(()->{
            if (this.agentState == AgentState.RUNNING) {
                this.agentState =AgentState.FINISHED;
                this.clearup();
            }
            log.info("SSE connection Completed");
        });


       return sseEmitter;
    }






    public void clearup(){

    }
}
