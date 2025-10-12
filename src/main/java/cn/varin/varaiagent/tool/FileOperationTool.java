package cn.varin.varaiagent.tool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Spring AI 文件操作工具类
 * 提供文件上传和下载功能
 */
public class FileOperationTool {


    // 文件存储根目录，可以根据实际情况修改
    private static final String FILE_STORAGE_DIR = System.getProperty("user.dir")+"/temp/files/";

    /**
     * 初始化存储目录
     */
    public FileOperationTool() {
        FileUtil.mkdir(FILE_STORAGE_DIR);
    }

    /**
     * 上传文件
     * @param fileName 文件名
     * @param content 文件内容
     * @return 上传结果信息
     */
    @Tool(name = "uploadFile", description = "上传文件到服务器")
    public String uploadFile(@ToolParam(description = "上传的文件名") String fileName, @ToolParam(description = "上传的内容") String content) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "错误：文件名不能为空";
        }
        
        if (content == null) {
            return "错误：文件内容不能为空";
        }
        
        try {
            String filePath = FILE_STORAGE_DIR + fileName;
            FileUtil.writeString(content, filePath, Charset.defaultCharset());
            return "文件上传成功！存储路径：" + filePath + "，文件大小：" + content.getBytes().length + "字节";
        } catch (Exception e) {
            return "文件上传失败：" + e.getMessage();
        }
    }

    /**
     * 下载文件
     * @param fileName 文件名
     * @return 文件内容，如果文件不存在则返回错误信息
     */
    @Tool(name = "downloadFile", description = "下载文件")
    public String downloadFile( @ToolParam(description = "要下载的文件名") String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return "错误：文件名不能为空";
        }
        
        String filePath = FILE_STORAGE_DIR + fileName;
        File file = new File(filePath);
        
        if (!FileUtil.exist(file)) {
            return "错误：文件不存在 - " + fileName;
        }
        
        try {
            return FileUtil.readString(file, Charset.defaultCharset());
        } catch (Exception e) {
            return "文件下载失败：" + e.getMessage();
        }
    }
}
