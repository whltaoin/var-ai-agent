package cn.varin.varaiagent.controller;

import cn.varin.varaiagent.app.IALDAApp;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.awt.*;

@CrossOrigin
@RestController
@RequestMapping("/IALDAApp")
public class IALDAAPPController {

    @Resource
    private IALDAApp ialdaApp;

    /**
     * 同步接口
     * @param content
     * @param chatId
     * @return
     */
    @GetMapping("/chat/sync")

    public String doChatWithAppSync(String content,String chatId) {


        return ialdaApp.getMessage(content, chatId);
    }

    /**
     * 流式调用1
     * @param content
     * @param chatId
     * @return
     */
    @GetMapping(value = "/chat/async1" ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)

    public Flux<String> doChatWithAppAsync(String content,String chatId) {


        return ialdaApp.getMessageByStream(content, chatId);
    }

    /**
     * 流式调用2
     * @param content
     * @param chatId
     * @return
     *
     *
     * 返回值：Flux<ServerSentEvent<String>>
     * Flux：是 Spring WebFlux（响应式编程框架）中的核心类，代表一个异步的、可流式发射 0 到多个元素的序列，适合处理持续的数据流。
     * ServerSentEvent：即 "服务器发送事件"（SSE），是一种 HTTP 协议的扩展，允许服务器通过单一 HTTP 连接向客户端持续推送数据（无需客户端反复请求），常用于实时通知、聊天消息等场景。
     */
    @GetMapping(value = "/chat/async2" )

    public Flux<ServerSentEvent<String>> doChatWithAppAsync2(String content, String chatId) {

        return ialdaApp.getMessageByStream(content, chatId)
                .map(s -> ServerSentEvent.<String>builder().data(s).build());
    }

    @GetMapping(value = "/chat/async3" )

    public SseEmitter doChatWithAppAsync3(String content, String chatId) {
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

    @GetMapping(value = "/ai" )

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
}
