package com.wipfli.training;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.wipfli.training")
//@EnableJpaRepositories(basePackages = "com.wipfli.training.repository")
//@EntityScan(basePackages = "com.wipfli.training.model")
public class PolicyCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(PolicyCenterApplication.class, args);
    }
}
