package cn.varin.varaiagent.tool;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * SpringAI工具类：通过指定API获取头像地址
 */
 // 交给Spring容器管理，确保@Tool注解被扫描
public class AvatarApiTool {

    // 目标API地址（用户指定的https://v2.api-m.com/api/head）
    private static final String AVATAR_API_URL = "https://v2.api-m.com/api/head";

    /**
     * 工具方法：调用头像API，解析并返回头像地址
     * @Tool注解：标记为SpringAI可调用的工具，name/description用于AI理解工具用途
     */
    @Tool(
        name = "getAvatarUrlFromApi", 
        description = "生成一张随机头像",
            returnDirect = true // 立即返回结果，
    )
    public String getAvatarUrl() {
        // 1. 用Hutool发起GET请求（根据API文档，此处为GET请求）
        try (HttpResponse response = HttpRequest.get(AVATAR_API_URL)
                .timeout(5000) // 超时时间5秒，避免阻塞
                .execute()) { // try-with-resources自动关闭响应流

            // 2. 检查HTTP响应状态码（先确保网络层请求成功）
            if (response.getStatus() != 200) {
                throw new RuntimeException(
                    String.format("头像API网络请求失败，HTTP状态码：%d，响应内容：%s",
                        response.getStatus(), response.body())
                );
            }

            // 3. 解析JSON响应（用户指定的响应格式：code/msg/data/request_id）
            String responseBody = response.body();
            if (!JSONUtil.isJson(responseBody)) { // 先判断是否为合法JSON
                throw new RuntimeException("头像API响应不是合法JSON格式，响应内容：" + responseBody);
            }

            JSONObject jsonObj = JSONUtil.parseObj(responseBody);

            // 4. 校验业务状态码（必须为200才返回头像地址）
            Integer code = jsonObj.getInt("code");
            if (code == null || !code.equals(200)) {
                String errorMsg = jsonObj.getStr("msg", "未知错误");
                throw new RuntimeException(
                    String.format("头像API业务请求失败，code：%d，msg：%s", code, errorMsg)
                );
            }

            // 5. 提取并返回头像地址（data字段）
            String avatarUrl = jsonObj.getStr("data");
            if (avatarUrl == null || avatarUrl.trim().isEmpty()) {
                throw new RuntimeException("头像API响应中data字段为空，无法获取头像地址");
            }

            return avatarUrl; // 返回最终的头像地址

        } catch (Exception e) {
            // 统一异常封装（便于上层处理）
            throw new RuntimeException("获取头像地址失败：" + e.getMessage(), e);
        }
    }
}