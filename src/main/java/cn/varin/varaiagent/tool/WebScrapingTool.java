package cn.varin.varaiagent.tool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WebScrapingTool {

    @Tool(description = "通过UR爬取网页源码")
    public String webScraping( @ToolParam(description = "抓取的URL") String url) {
        try {
            Document elements = Jsoup.connect(url).get();
            return elements.html();
        }catch (Exception e){
            return "抓取失败："+e.getMessage();
        }

    }
}
