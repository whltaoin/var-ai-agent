package cn.varin.varaiagent.tool;

import cn.varin.varaiagent.properties.EmailProperties;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

/**
 * 邮件发送工具类（使用外部配置）
 * 支持通过@Tool注解被AI调用，实现发送邮件功能
 */
@Component
public class EmailSenderTool {

    // 注入邮件配置属性
    private final EmailProperties emailProperties;

    // 构造函数注入配置
    @Autowired
    public EmailSenderTool(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    /**
     * 发送邮件
     * @param myEmail 发送者邮箱地址（需包含SMTP信息）
     * @param recipientEmail 接收者邮箱地址
     * @param title 邮件标题
     * @param content 邮件内容
     * @return 发送结果信息
     */
    @Tool(
        name = "sendEmail",
        description = "发送邮件到指定邮箱"
    )
    public String sendEmail(@ToolParam(description = "发送方邮箱") String myEmail,
                            @ToolParam(description = "接收方邮箱")    String recipientEmail,
                            @ToolParam(description = "邮件标题")   String title,
                            @ToolParam(description = "邮件内容")  String content) {
        // 参数校验
        if (StrUtil.isBlank(myEmail)) {
            return "发送失败：发送者邮箱不能为空";
        }
        if (StrUtil.isBlank(recipientEmail)) {
            return "发送失败：接收者邮箱不能为空";
        }
        if (StrUtil.isBlank(title)) {
            return "发送失败：邮件标题不能为空";
        }
        if (StrUtil.isBlank(content)) {
            return "发送失败：邮件内容不能为空";
        }
        
        try {
            // 解析发送者邮箱，提取用户名和域名
            String[] emailParts = myEmail.split("@");
            if (emailParts.length != 2) {
                return "发送失败：发送者邮箱格式不正确";
            }
            String domain = emailParts[1].toLowerCase();
            
            // 创建邮件账户配置，从配置文件获取信息
            MailAccount account = new MailAccount();
            
            // 获取SMTP服务器信息（优先使用配置文件中的，没有则使用默认映射）
            String smtpHost = getSmtpHostByDomain(domain);
            Integer smtpPort = getSmtpPortByDomain(domain);
            
            account.setHost(smtpHost);
            account.setPort(smtpPort);
            account.setFrom(myEmail);
            account.setUser(emailParts[0]);
            account.setPass(emailProperties.getPassword()); // 使用配置文件中的授权码
            account.setAuth(true); // 启用认证
            account.setStarttlsEnable(true); // 启用TLS加密
            
            // 发送邮件
            MailUtil.send(account, recipientEmail, title, content, false);
            
            return "邮件发送成功！\n发送者：" + myEmail + 
                   "\n接收者：" + recipientEmail + 
                   "\n标题：" + title;
        } catch (Exception e) {
            return "邮件发送失败：" + e.getMessage() + 
                   "\n请检查邮箱配置（特别是SMTP服务器和授权码）是否正确";
        }
    }
    
    /**
     * 根据邮箱域名获取对应的SMTP服务器地址
     * 优先使用配置文件中的配置，没有则使用默认值
     */
    private String getSmtpHostByDomain(String domain) {
        // 优先使用配置文件中的全局配置
        if (StrUtil.isNotBlank(emailProperties.getHost())) {
            return emailProperties.getHost();
        }
        
        // 根据域名选择对应服务商的配置
        switch (domain) {
            case "qq.com":
                return emailProperties.getQq().getHost();
            case "163.com":
            case "126.com":
                return emailProperties.getNetease().getHost();
            case "gmail.com":
                return emailProperties.getGmail().getHost();
            case "outlook.com":
            case "hotmail.com":
                return emailProperties.getOutlook().getHost();
            default:
                // 默认使用通用格式
                return "smtp." + domain;
        }
    }
    
    /**
     * 根据邮箱域名获取对应的SMTP端口
     * 优先使用配置文件中的配置，没有则使用默认值
     */
    private Integer getSmtpPortByDomain(String domain) {
        // 优先使用配置文件中的全局配置
        if (emailProperties.getPort() != null) {
            return emailProperties.getPort();
        }
        
        // 根据域名选择对应服务商的端口
        switch (domain) {
            case "qq.com":
                return emailProperties.getQq().getPort();
            case "163.com":
            case "126.com":
                return emailProperties.getNetease().getPort();
            case "gmail.com":
                return emailProperties.getGmail().getPort();
            case "outlook.com":
            case "hotmail.com":
                return emailProperties.getOutlook().getPort();
            default:
                // 默认端口
                return 587;
        }
    }
}
    