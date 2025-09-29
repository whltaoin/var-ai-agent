package cn.varin.varaiagent.advisors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

@Slf4j
public class MyLogAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    /**
     * 非流式同步方法
     * @param advisedRequest the advised request
     * @param chain the advisor chain
     * @return
     */
    @Override
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

        this.beforeLog(advisedRequest);
        // 获取到响应
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.afterLog(advisedResponse);

        return advisedResponse;
    }

    /**
     * 流式异步方法
     * @param advisedRequest the advised request
     * @param chain the chain of advisors to execute
     * @return
     */
    @Override
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        this.beforeLog(advisedRequest);
        Flux<AdvisedResponse> flux = chain.nextAroundStream(advisedRequest);
      return (new MessageAggregator()).aggregateAdvisedResponse(flux, this::afterLog);
    }



    /**
     * 提供唯一标识名
     * @return
     */

    @Override
    public String getName() {
        return "var 的 Advisor";
    }

    /**
     * 设置执行顺序
     * @return
     */

    @Override
    public int getOrder() {
        return 100;
    }

    /**
     * 定义一个请求前日志信息
     */
    public void beforeLog(AdvisedRequest advisedRequest) {

        log.info("beforeName:{}", "varin");
        log.info("advisedRequest:{}", advisedRequest);

    }
    /**
     * 定义一个响应后日志信息
     */
    public void afterLog(AdvisedResponse advisedResponse) {
        log.info("afterName:{}", "varin");

        log.info("advisedRequest:{}", advisedResponse);

    }
}
