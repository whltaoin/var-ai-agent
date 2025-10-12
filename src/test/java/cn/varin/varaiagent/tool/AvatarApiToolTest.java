package cn.varin.varaiagent.tool;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AvatarApiToolTest {

    @Test
    void getAvatarUrl() {
        AvatarApiTool avatarApiTool = new AvatarApiTool();
        String avatarUrl = avatarApiTool.getAvatarUrl();
        log.info(avatarUrl);
    }
}