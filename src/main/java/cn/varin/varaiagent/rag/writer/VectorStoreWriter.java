package cn.varin.varaiagent.rag.writer;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据写入到向量数据库中
 */
@Component
public class VectorStoreWriter {
    private final VectorStore vectorStore;
    public VectorStoreWriter(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    public void writer(List<Document> documents) {
        vectorStore.accept(documents);

    }

}
