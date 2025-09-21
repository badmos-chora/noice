package com.noice.productbff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditingAwareImpl")
public class ProductBffApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductBffApplication.class, args);
    }

}
