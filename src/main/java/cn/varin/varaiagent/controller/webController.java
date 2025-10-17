package cn.varin.varaiagent.controller;

import cn.varin.varaiagent.agent.VarinManus;
import cn.varin.varaiagent.app.IALDAApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@CrossOrigin
@RestController
@RequestMapping("/ai")
public class webController {


    @Resource
    private IALDAApp ialdaApp;

    @GetMapping(value = "/app/sse" )

    public SseEmitter doChatWithAppAsync4(String content, String chatId) {
        SseEmitter sseEmitter = new SseEmitter(30000L);

        ialdaApp.getMessageByStream(content, chatId)
                .subscribe(
                        data->{
                            try {
                                sseEmitter.send(data);

                            }catch (Exception e){
                                sseEmitter.completeWithError(e);

                            }
                        }
                        ,
                        sseEmitter::completeWithError,// 错误执行
                        sseEmitter::complete//成功执行

                );
        return sseEmitter;

    }

    @Resource
    private ToolCallback[] allTools;
    @Resource
    private ChatModel dashScopeChatModel;

    @GetMapping("/manus/chat")
    public SseEmitter runStream(String userPrompt) {

        VarinManus varinManus = new VarinManus(allTools, dashScopeChatModel);
        return   varinManus.runStream(userPrompt);
    }
}
