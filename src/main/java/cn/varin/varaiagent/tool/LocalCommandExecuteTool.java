package cn.varin.varaiagent.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

/**
 * Spring AI 本地指令执行工具类（基于Process API）
 * 仅允许白名单指令，避免恶意指令风险
 */
@Component
public class LocalCommandExecuteTool {

    // 【安全关键】指令白名单：仅允许执行以下预设指令，禁止直接执行用户自定义指令
    private static final Set<String> ALLOWED_COMMANDS = Set.of(
        "ipconfig",          // Windows查看IP配置
            "ifconfig",
        "ping localhost",    // 测试本地网络连通性
        "dir",               // Windows查看当前目录文件（需注意路径）
        "ls",                // Linux/Mac查看当前目录文件（需注意路径）
        "echo hello"         // 简单输出测试
    );

    /**
     * 执行本地指令（仅允许白名单内指令）
     * @param command 用户输入的指令（需在白名单内）
     * @return 指令执行结果（包含输出和错误信息）
     */
    @Tool(
        name = "executeLocalCommand",
        description = "输入命令执行指令"
    )
    public String executeLocalCommand(@ToolParam(description = "要执行的指令") String command) {
        // 1. 基础参数校验
        if (StrUtil.isBlank(command)) {
            return "执行失败：指令不能为空";
        }

        // 2. 安全校验：仅允许白名单指令
        if (!ALLOWED_COMMANDS.contains(command.trim())) {
            return "执行失败：当前指令不在安全白名单内，禁止执行（允许指令：" + 
                   CollectionUtil.join(ALLOWED_COMMANDS, "、") + "）";
        }

        // 3. 处理系统差异：Windows用cmd.exe，Linux/Mac用/bin/bash
        String os = System.getProperty("os.name").toLowerCase();
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (os.contains("win")) {
            processBuilder.command("cmd.exe", "/c", command);
        } else {
            processBuilder.command("/bin/bash", "-c", command);
        }

        // 4. 执行指令并捕获输出
        StringBuilder result = new StringBuilder();
        Process process = null;
        try {
            // 启动进程
            process = processBuilder.start();

            // 读取标准输出流
            readStream(process.getInputStream(), result, "输出：");
            // 读取错误输出流（避免进程阻塞）
            readStream(process.getErrorStream(), result, "错误：");

            // 等待进程执行完成
            int exitCode = process.waitFor();
            result.append("\n指令执行完成，退出码：").append(exitCode);

        } catch (IOException e) {
            result.append("\n执行异常（IO错误）：").append(e.getMessage());
        } catch (InterruptedException e) {
            result.append("\n执行异常（进程被中断）：").append(e.getMessage());
            Thread.currentThread().interrupt(); // 恢复中断状态
        } finally {
            // 关闭进程（防止资源泄漏）
            if (process != null && process.isAlive()) {
                process.destroy();
            }
        }

        return result.toString();
    }

    /**
     * 读取进程的输入流（标准输出/错误输出），并追加到结果中
     * @param inputStream 进程流
     * @param result 结果拼接对象
     * @param prefix 流内容前缀（区分输出/错误）
     */
    private void readStream(java.io.InputStream inputStream, StringBuilder result, String prefix) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(prefix).append(line).append("\n");
            }
        } catch (IOException e) {
            result.append("\n读取流异常：").append(e.getMessage());
        }
    }
}