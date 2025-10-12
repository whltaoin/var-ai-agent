package cn.varin.varaiagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Spring AI 联网搜索工具类（基于Hutool HttpUtil实现）
 * 使用Hutool简化网络请求，提取organic_results核心集合
 */
public class WebSearchApiTool {
    private String apiKey;
    public WebSearchApiTool(String apiKey) {
        this.apiKey = apiKey;
    }

    // SearchAPI基础URL（与之前一致）
    private static final String SEARCH_API_BASE_URL = "https://www.searchapi.io/api/v1/search";

    /**
     * 基于Hutool HttpUtil联网搜索，提取organic_results集合

     * @param query 搜索关键词（如"ERNIE Bot"）
     * @return 搜索结果集合（List<JSONObject>），异常时返回空集合
     */
    @Tool(
        name = "hutoolSearchAndExtractOrganicResults",
        description = "从百度搜索引擎搜索信息"
    )
    public List<JSONObject> searchAndExtractOrganicResults( @ToolParam(description = "搜索查询关键字") String query) {

        if (StrUtil.isBlank(query)) {
            throw new IllegalArgumentException("参数错误：搜索关键词（query）不能为空");
        }

        try {
            // 2. 构建请求参数（Hutool HttpUtil支持Map传参，自动拼接URL）
            Map<String, Object> params = Map.of(
                "engine", "baidu",
                "api_key", apiKey,

                "q", query
            );

            // 3. 发送GET请求（Hutool HttpUtil自动处理URL编码和响应解码）
            String responseBody = HttpUtil.get(SEARCH_API_BASE_URL, params);

            // 4. 解析JSON响应（Hutool JsonUtil简化JSON处理）
            JSONObject responseJson = JSONUtil.parseObj(responseBody);

            // 5. 提取organic_results数组（转为JSONObject集合，避免强依赖实体类）
            return responseJson.getJSONArray("organic_results") != null 
                ? responseJson.getJSONArray("organic_results").toList(JSONObject.class) 
                : Collections.emptyList();

        } catch (Exception e) {
            // 异常处理（打印日志，实际开发建议用SLF4J）
            System.err.println("Hutool搜索工具执行异常：" + e.getMessage());
            return Collections.emptyList();
        }
    }
}