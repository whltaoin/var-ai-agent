package cn.varin.varaiagent.config;

import cn.varin.varaiagent.tool.*;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegisterConfig {
    @Value("${searchApi.api_key}")
    private String apiKey;

    @Bean
    public ToolCallback[] allTools() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        LocalCommandExecuteTool localCommandExecuteTool = new LocalCommandExecuteTool();
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        WebScrapingTool webScrapingTool = new WebScrapingTool();
        WebSearchApiTool webSearchApiTool = new WebSearchApiTool(apiKey);
        AvatarApiTool avatarApiTool = new AvatarApiTool();


        ToolCallback[] from = ToolCallbacks.from(
                fileOperationTool,
                localCommandExecuteTool,
                pdfGenerationTool,
                resourceDownloadTool,
                webScrapingTool,
                webSearchApiTool
    ,avatarApiTool

        );
        return from;

    }
}
