package cn.varin.varaiagent.rag.enricher;

import cn.varin.varaiagent.config.AliyunConfig;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.DefaultContentFormatter;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.SummaryMetadataEnricher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 自定义文档元信息增强器
 */

@Component
public class MyDocumentEnricher {
    private final ChatModel chatModel;

    private MyDocumentEnricher(ChatModel chatModel) {
        this.chatModel = chatModel;

    }

    /**
     * 关键词
     * @param documents
     * @return
     */
    List<Document> documentEnricherByKeyWord(List<Document> documents) {
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
        List<Document> documentList = keywordMetadataEnricher.apply(documents);
        return documentList;
    }

    /**
     * 摘要
     * @param documents
     * @return
     */
    List<Document> documentEnricherBySummary(List<Document> documents) {
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel,

                List.of(SummaryMetadataEnricher.SummaryType.PREVIOUS, // 关联上文
                        SummaryMetadataEnricher.SummaryType.CURRENT,// 关联当前文章
                        SummaryMetadataEnricher.SummaryType.NEXT));// 关联下文
        List<Document> documentList = summaryMetadataEnricher.apply(documents);
        return documentList;
    }

}
