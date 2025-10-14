package cn.varin.varinimagesearchmcp;

import cn.varin.varinimagesearchmcp.tool.PexelsImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VarinImageSearchMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(VarinImageSearchMcpApplication.class, args);
    }

    /**
     * 将tool对象转成MCP
     * @param pexelsImageSearchTool
     * @return
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(PexelsImageSearchTool pexelsImageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(pexelsImageSearchTool)
                .build();
    }
}
