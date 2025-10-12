package cn.varin.varaiagent.rag.etl;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.transformer.KeywordMetadataEnricher;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.writer.FileDocumentWriter;

import java.util.List;

public class ETLMain {
    public static void main(String[] args) {
        //E
        MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader("document.md");
        List<Document> documentList = markdownDocumentReader.read();

        // T1
        TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
       documentList = tokenTextSplitter.apply(documentList);
        //T2
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(null, 3);
      documentList= keywordMetadataEnricher.apply(documentList);
      //L 文件存储
        FileDocumentWriter fileDocumentWriter = new FileDocumentWriter(null);
        fileDocumentWriter.write(documentList);


    }
}
