package cn.varin.varinimagesearchmcp.tool;

import cn.varin.varinimagesearchmcp.entity.PexelsImage;
import cn.varin.varinimagesearchmcp.entity.PexelsResponse;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PexelsImageSearchTool {

    @Value("${imageSearch.api_key}")
    private String apiKey;
    
    private static final String PEXELS_SEARCH_URL = "https://api.pexels.com/v1/search";
    private static final int DEFAULT_PER_PAGE = 5; // 默认返回5张图片

    @Tool( description = "根据图片类型查询到对应类型的图片")
    public String searchImages( @ToolParam(description = "图片的类型") String type) {
        // 设置请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", apiKey);
        
        // 设置请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("query", type);
        params.put("per_page", DEFAULT_PER_PAGE);
        
        // 发送GET请求
        String result = HttpUtil.createGet(PEXELS_SEARCH_URL)
                .addHeaders(headers)
                .form(params)
                .execute()
                .body();
        
        // 解析JSON响应
        PexelsResponse response = JSONUtil.toBean(result, PexelsResponse.class);
        StringBuffer stringBuffer = new StringBuffer();
        // 返回图片列表
        response.getPhotos().forEach(pexelsImage -> {
            String original = pexelsImage.getSrc().getOriginal();
            stringBuffer.append(original+"\n");

        });
        return stringBuffer.toString();
    }
}
