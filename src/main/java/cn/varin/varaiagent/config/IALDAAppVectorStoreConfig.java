package cn.varin.varaiagent.config;


import cn.varin.varaiagent.rag.IALDAAppDocumentLoader;
import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
/**
 * SimpleVectorStore基于内存读写的向量数据库
 */

public class IALDAAppVectorStoreConfig {

    @Resource
    private IALDAAppDocumentLoader iALDAAppDocumentLoader;
    // 注册VectorSotre

    //注意：EmbeddingModel使用SpringAI的，不要使用alibaba的
    @Bean
    VectorStore iALDAAppVectorStore(EmbeddingModel environment) {
      VectorStore  vectorStore = SimpleVectorStore.builder(environment).build();


        List<Document> documents = iALDAAppDocumentLoader.loadDocuments();
        vectorStore.add(documents);
        return vectorStore;

    }
}
