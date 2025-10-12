package cn.varin.varaiagent.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class WebScrapingToolTest {

    @Test
    void webScraping() {
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        String s = webScrapingTool.webScraping("http://www.baidu.com");
    }
}