package cn.varin.varaiagent.invoke;
//http接入大模型
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.varin.varaiagent.config.AliyunConfig;

public class HttpInvoke {
    public static void main(String[] args) {
        // 替换为你的API密钥
        String apiKey = AliyunConfig.accessKeyId;
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("请设置环境变量DASHSCOPE_API_KEY");
            return;
        }

        // 构建请求URL
        String url = "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions";

        // 构建请求体JSON
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "qwen-plus");

        // 构建messages数组
        JSONArray messages = new JSONArray();

        // 添加system消息
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", "You are a helpful assistant.");
        messages.add(systemMsg);

        // 添加user消息
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", "给我介绍一本关于学习Java的书籍");
        messages.add(userMsg);

        requestBody.put("messages", messages);

        // 发送POST请求
        HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .execute();

        // 输出响应结果
        System.out.println("响应状态码: " + response.getStatus());
        System.out.println("响应内容: " + response.body());
    }
}
