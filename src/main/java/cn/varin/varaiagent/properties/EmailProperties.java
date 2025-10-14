package cn.varin.varaiagent.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 邮件配置属性类
 * 用于绑定application.yml中的邮件配置
 */
@Component
@ConfigurationProperties(prefix = "email")
public class EmailProperties {
    // 默认SMTP服务器配置
    private String host;
    private Integer port = 587;
    private String from;
    private String user;
    private String password; // 这里存储授权码
    
    // 各邮箱服务商的SMTP配置映射（可选，用于多邮箱支持）
    private MailProviderConfig qq = new MailProviderConfig("smtp.qq.com", 587);
    private MailProviderConfig netease = new MailProviderConfig("smtp.163.com", 587);
    private MailProviderConfig gmail = new MailProviderConfig("smtp.gmail.com", 587);
    private MailProviderConfig outlook = new MailProviderConfig("smtp.office365.com", 587);
    
    // 内部类：邮箱服务商配置
    public static class MailProviderConfig {
        private String host;
        private Integer port;
        
        public MailProviderConfig() {}
        
        public MailProviderConfig(String host, Integer port) {
            this.host = host;
            this.port = port;
        }
        
        // Getter和Setter
        public String getHost() {
            return host;
        }
        
        public void setHost(String host) {
            this.host = host;
        }
        
        public Integer getPort() {
            return port;
        }
        
        public void setPort(Integer port) {
            this.port = port;
        }
    }
    
    // Getter和Setter
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public Integer getPort() {
        return port;
    }
    
    public void setPort(Integer port) {
        this.port = port;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public MailProviderConfig getQq() {
        return qq;
    }
    
    public void setQq(MailProviderConfig qq) {
        this.qq = qq;
    }
    
    public MailProviderConfig getNetease() {
        return netease;
    }
    
    public void setNetease(MailProviderConfig netease) {
        this.netease = netease;
    }
    
    public MailProviderConfig getGmail() {
        return gmail;
    }
    
    public void setGmail(MailProviderConfig gmail) {
        this.gmail = gmail;
    }
    
    public MailProviderConfig getOutlook() {
        return outlook;
    }
    
    public void setOutlook(MailProviderConfig outlook) {
        this.outlook = outlook;
    }
}
    