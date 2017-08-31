package com.vrrpg.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class VrRpgServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VrRpgServerApplication.class, args);
    }
}
