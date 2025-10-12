package cn.varin.varaiagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(exclude =  PgVectorVectorStoreConfig.class )
@SpringBootApplication


public class VarAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(VarAiAgentApplication.class, args);
    }

}
