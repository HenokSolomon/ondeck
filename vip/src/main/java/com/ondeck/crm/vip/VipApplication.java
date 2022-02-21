package com.ondeck.crm.vip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VipApplication {

    public static void main(String[] args) {
        SpringApplication.run( VipApplication.class, args );
    }

}
