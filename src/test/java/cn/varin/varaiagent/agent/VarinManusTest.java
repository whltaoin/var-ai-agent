package cn.varin.varaiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VarinManusTest {

    @Resource
    private VarinManus varinManus;
    @Test
    public void run() {
        String userPrompt = """  
               我现在需要将内容3333写入到abc.text文件中
                """;
        String result = varinManus.run(userPrompt);
        Assertions.assertNotNull(result);

    }

}