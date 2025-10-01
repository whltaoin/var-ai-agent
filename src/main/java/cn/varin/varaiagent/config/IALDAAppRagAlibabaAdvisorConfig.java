package cn.varin.varaiagent.config;


import cn.varin.varaiagent.rag.IALDAAppDocumentLoader;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
/**
 * 从阿里云上读取知识库，并使用对应的advisor
 * 知识库地址：https://bailian.console.aliyun.com/?tab=app#/knowledge-base
 */

public class IALDAAppRagAlibabaAdvisorConfig {
    @Value("${spring.ai.dashscope.api-key}")
    private String apikey;


    @Bean
    public Advisor iALDAAppRagAlibabaAdvisor() {
     DashScopeApi dashScopeApi = new DashScopeApi(apikey);
     final String DOCUMENT_NAME = "学术分析";
        DashScopeDocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(dashScopeApi
        ,new DashScopeDocumentRetrieverOptions.Builder()
                .withIndexName(DOCUMENT_NAME)
                .build()

        );

        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(dashScopeDocumentRetriever)
                .build();

    }

}
