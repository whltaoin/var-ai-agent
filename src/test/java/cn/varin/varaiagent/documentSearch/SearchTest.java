package cn.varin.varaiagent.documentSearch;

import cn.varin.varaiagent.test.ChatClientTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SearchTest {

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 查询重写
     */
    @Test
    public void reWriterQueryTransformerTest() {

        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        Query query = new Query("你告诉我，什么是RAG呀什么是，什么是呀？？");

        QueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();

        Query transformedQuery = queryTransformer.transform(query);
        System.out.println(transformedQuery.toString());
    }
    /**
     * 查询翻译
     */

    @Test
    public void Test() {

        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        Query query = new Query("你告诉我，什么是RAG呀什么是，什么是呀？？");

        QueryTransformer queryTransformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(builder)
                .targetLanguage("English")
                .build();

        Query transformedQuery = queryTransformer.transform(query);
        System.out.println(transformedQuery.toString());
    }

    /**
     * 查询压缩
     *
     */

    @Test
    public void Test2() {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        CompressionQueryTransformer queryTransformer =  CompressionQueryTransformer.builder()
                .chatClientBuilder(builder).build();
        Query query = Query.builder()
                .text("什么事RAG？")
                .history(new UserMessage("谁会RAG？"),
                        new AssistantMessage("我的网站 varin.cn"))
                .build();
        Query transformedQuery = queryTransformer.transform(query);
        System.out.println(transformedQuery.toString());


    }

    /**
     * 查询扩展
     *
     */

    @Test
    public void Test3() {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(builder)
                .numberOfQueries(5)
                .build();
        List<Query> expand = multiQueryExpander.expand(new Query("CSDN博主Varin技术这么样？"));
        expand.forEach(System.out::println);

    }
}

