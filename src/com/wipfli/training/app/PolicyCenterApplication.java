package com.wipfli.training.app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = "com.wipfli.training")
public class PolicyCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyCenterApplication.class, args);
    }
}
