package cn.varin.varaiagent.controller;

import cn.varin.varaiagent.agent.VarinManus;
import cn.varin.varaiagent.config.ToolRegisterConfig;
import com.alibaba.dashscope.tools.ToolCallBase;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/varinAgent")
public class VarinAgentController {
    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashScopeChatModel;

    @GetMapping("/chat/runStream")
    public SseEmitter runStream(String userPrompt) {

        VarinManus varinManus = new VarinManus(allTools, dashScopeChatModel);
      return   varinManus.runStream(userPrompt);
    }
}
