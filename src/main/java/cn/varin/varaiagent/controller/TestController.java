package cn.varin.varaiagent.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@Tag(name="测试接口")

@RequestMapping("/test")
public class TestController {
    @GetMapping("/hello")
    private String hello() {
        return "hello";
    }
}
