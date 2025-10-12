package cn.varin.varaiagent.tool;

import cn.hutool.http.HttpUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;
import java.util.UUID;

/**
 * Spring AI资源下载工具类
 * 使用Hutool的HttpUtil实现网络资源下载
 */
@Component
public class ResourceDownloadTool {

    // 下载文件存储的根目录，可根据实际需求修改
    private static final String FILE_STORAGE_DIR = System.getProperty("user.dir")+"/temp/download/";

    /**
     * 初始化下载目录
     */
    public ResourceDownloadTool() {
        // 确保下载目录存在，不存在则自动创建
        FileUtil.mkdir(FILE_STORAGE_DIR);
    }

    /**
     * 下载网络资源到本地
     * @param resourceUrl 资源的URL地址（必填）
     * @param fileName 保存的文件名（可选，为null时自动生成）
     * @return 下载结果信息，包含文件保存路径或错误信息
     */
    @Tool(
        name = "downloadResource",
        description = "从给定的URL下载资源"
    )
    public String downloadResource(@ToolParam(description = "下载地址") String resourceUrl,@ToolParam(description = "资源名称") String fileName) {
        // 1. 校验URL参数
        if (StrUtil.isBlank(resourceUrl)) {
            return "下载失败：资源URL不能为空";
        }
        
        // 2. 处理文件名（如果未指定则自动生成）
        String saveFileName = processFileName(resourceUrl, fileName);
        if (StrUtil.isBlank(saveFileName)) {
            return "下载失败：无法确定有效的文件名";
        }
        
        // 3. 构建完整的保存路径
        String savePath = FILE_STORAGE_DIR + saveFileName;
        File saveFile = FileUtil.file(savePath);

        
        try {
            // 4. 使用Hutool的HttpUtil下载文件
            Long downloadedFileSize = HttpUtil.downloadFile(resourceUrl, FileUtil.file(savePath));

            // 5. 验证下载结果
            if (downloadedFileSize > 0) {
                return "下载成功！文件保存路径：" + saveFile.getAbsolutePath() +
                        "，实际下载大小：" + FileUtil.readableFileSize(downloadedFileSize);
            } else if (downloadedFileSize == 0) {
                // 清理空文件
                FileUtil.del(saveFile);
                return "下载失败：文件大小为0字节（可能是无效URL或空资源）";
            } else {
                // 某些情况下可能返回-1表示失败
                return "下载失败：Hutool下载工具返回异常状态码";
            }
        } catch (Exception e) {
            // 6. 处理下载异常
            return "下载失败：" + e.getMessage() + "，URL：" + resourceUrl;
        }
    }

    /**
     * 处理文件名，确保文件名有效
     * @param resourceUrl 资源URL
     * @param fileName 用户指定的文件名（可能为null）
     * @return 处理后的有效文件名
     */
    private String processFileName(String resourceUrl, String fileName) {
        // 如果用户指定了文件名且不为空，则使用用户提供的文件名
        if (StrUtil.isNotBlank(fileName)) {
            // 移除文件名中的特殊字符，确保安全
            return FileUtil.cleanInvalid(fileName);
        }
        
        // 如果未指定文件名，则从URL中提取或生成唯一文件名
        String urlFileName = FileUtil.getName(resourceUrl);
        if (StrUtil.isNotBlank(urlFileName)) {
            // 从URL提取的文件名不为空时，使用该文件名
            return FileUtil.cleanInvalid(urlFileName);
        }
        
        // 如果URL中无法提取文件名，则生成UUID作为文件名
        return UUID.randomUUID().toString();
    }
}
