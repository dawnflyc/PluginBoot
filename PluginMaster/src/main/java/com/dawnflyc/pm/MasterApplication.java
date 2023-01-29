package com.dawnflyc.pm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.dawnflyc")
public class MasterApplication {

    public static void load(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }

}
