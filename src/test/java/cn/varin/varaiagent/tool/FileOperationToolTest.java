package cn.varin.varaiagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileOperationToolTest {

    @Test
    void uploadFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String s = fileOperationTool.uploadFile("test.txt", "test");
        System.out.println(s);
    }

    @Test
    void downloadFile() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String s = fileOperationTool.downloadFile("test.txt");
        System.out.println(s);
    }
}