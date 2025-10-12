package cn.varin.varaiagent.rag.writer;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 写入文件
 */
@Component
public class MyFileDocumentWriter {

    /**
     *
     * @param documentList
     * @param fileName
     */
    public void writer(List<Document> documentList,String fileName) {
        FileDocumentWriter fileDocumentWriter = new FileDocumentWriter(fileName,true, MetadataMode.ALL,true);
        fileDocumentWriter.write(documentList);
    }



}
