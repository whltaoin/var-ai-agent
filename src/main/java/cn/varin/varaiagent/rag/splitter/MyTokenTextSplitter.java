package cn.varin.varaiagent.rag.splitter;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义的文本分割器
 */
@Component
public class MyTokenTextSplitter {
    // 使用无参数的TokenTextSplitter
    public List<Document> splitDocument(List<Document> documents) {
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
        return tokenTextSplitter.apply(documents);
    }
    // 使用参数的TokenTextSplitter
    public List<Document> splitStructure(List<Document> documents) {
        TokenTextSplitter tokenTextSplitter =  new TokenTextSplitter(1000, 400, 10, 5000, true);
        return tokenTextSplitter.apply(documents);
    }

}
