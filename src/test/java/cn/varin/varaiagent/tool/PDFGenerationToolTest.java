package cn.varin.varaiagent.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        try {
            String s = pdfGenerationTool.generatePDF("text.pdf", "abc");
            log.info(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}