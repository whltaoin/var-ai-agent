package cn.varin.varinimagesearchmcp.tool;

import cn.varin.varinimagesearchmcp.entity.PexelsImage;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PexelsImageSearchToolTest {
    @Resource
    private PexelsImageSearchTool pexelsImageSearchTool;

    @Test
    void searchImages() {
        String s = pexelsImageSearchTool.searchImages("computer");
        assertNotNull(s);
    }
}