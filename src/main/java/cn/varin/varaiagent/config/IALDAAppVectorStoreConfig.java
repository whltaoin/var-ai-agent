package cn.varin.varaiagent.config;


import cn.varin.varaiagent.rag.IALDAAppDocumentLoader;
import cn.varin.varaiagent.rag.splitter.MyTokenTextSplitter;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentWriter;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

@Configuration
/**
 *向量数据库配置类：
 *
 */

public class IALDAAppVectorStoreConfig {
    /**
     * SimpleVectorStore基于内存读写的向量数据库
     */

    @Resource
    private IALDAAppDocumentLoader iALDAAppDocumentLoader;
    // 文档分割器
    @Resource
    private MyTokenTextSplitter myTokenTextSplitter;
    // 注册VectorSotre：

    //注意：EmbeddingModel使用SpringAI的，不要使用alibaba的
    @Bean
    VectorStore iALDAAppVectorStore(EmbeddingModel environment) {
      VectorStore  vectorStore = SimpleVectorStore.builder(environment).build();

        System.out.println("vectorStore = " + vectorStore.getClass().getName());
        List<Document> documents = iALDAAppDocumentLoader.loadDocuments();
//        //拿到了文档，进行切割在存储:注意：效果不好，不建议使用，建议使用云平台的
//        List<Document> splitDocuments = myTokenTextSplitter.splitStructure(documents);

        vectorStore.add(documents);
        return vectorStore;

    }

    /**
     * 阿里云PGVectorStore
     * @param jdbcTemplate
     * @param dashscopeEmbeddingModel
     * @return
     */

    // @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)
                .distanceType(COSINE_DISTANCE)
                .indexType(HNSW)
                .initializeSchema(true)
                .schemaName("public")
                .vectorTableName("vector_store")
                .maxDocumentBatchSize(10000)
                .build();

        List<Document> documents =iALDAAppDocumentLoader.loadDocuments();
        vectorStore.add(documents);
        return vectorStore;
    }
}
