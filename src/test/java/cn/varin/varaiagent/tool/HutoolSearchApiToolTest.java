package cn.varin.varaiagent.tool;

import cn.hutool.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HutoolSearchApiToolTest {
    @Value("${searchApi.api_key}")
    private String apiKey;

    @Test
    void searchAndExtractOrganicResults() {
        WebSearchApiTool searchApiTool = new WebSearchApiTool(apiKey);
        List<JSONObject> results = searchApiTool.searchAndExtractOrganicResults("三明天气");
        results.forEach(System.out::println);
        System.out.println(results.size());
    }
}