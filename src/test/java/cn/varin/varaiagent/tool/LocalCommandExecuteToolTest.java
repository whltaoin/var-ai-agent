package cn.varin.varaiagent.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class LocalCommandExecuteToolTest {

    @Test
    void executeLocalCommand() {
        LocalCommandExecuteTool localCommandExecuteTool = new LocalCommandExecuteTool();
        String s = localCommandExecuteTool.executeLocalCommand("ifconfig");
        log.info(s);
    }
}