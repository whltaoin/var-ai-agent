package cn.varin.varaiagent.app;


import cn.varin.varaiagent.promptTamplate.MyPromptTemplate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest

public class IALDAAppTest {

    @Resource
    private IALDAApp ialdaApp;

    @Test
    public void test() {}
    @Test
    public void getMessage() {
        String uuid= UUID.randomUUID().toString();
         ialdaApp.getMessage("你好，我是varin", uuid);
         ialdaApp.getMessage("我是软件工程专业的", uuid);
         ialdaApp.getMessage("我叫什么？，我需要方向的论文指导？", uuid);

    }
    @Test
    public void testMyAdvisor() {
        String uuid= UUID.randomUUID().toString();
        ialdaApp.getMessage("你好，我是varin", uuid);

    }

    @Test
    public void fileSaveMessageTest() {
        String uuid= UUID.randomUUID().toString();
        ialdaApp.fileSaveMessage("你好，我是varin", uuid);

    }

    @Test
    public void promptTemplateTest() {
        MyPromptTemplate myPromptTemplate = new MyPromptTemplate("varin","软件工程");
        String uuid= UUID.randomUUID().toString();
        ialdaApp.fileSaveMessage(myPromptTemplate.getPrompt(), uuid);

    }
}