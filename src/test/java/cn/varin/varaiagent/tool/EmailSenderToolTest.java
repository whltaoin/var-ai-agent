package cn.varin.varaiagent.tool;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmailSenderToolTest {

    @Autowired
    private EmailSenderTool emailSenderTool;

    @Test
    void sendEmail() {
        String s = emailSenderTool.sendEmail("whltaoin@163.com", "3310923053@qq.com", "test", "test");
        Assertions.assertNotNull(s);
    }
}