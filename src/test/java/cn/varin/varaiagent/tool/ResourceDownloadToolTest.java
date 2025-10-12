package cn.varin.varaiagent.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String s = resourceDownloadTool.downloadResource("https://www.baidu.com/", "index.html");
        log.info(s);
    }
}