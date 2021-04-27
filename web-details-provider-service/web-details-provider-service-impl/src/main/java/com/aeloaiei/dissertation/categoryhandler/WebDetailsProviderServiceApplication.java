package com.aeloaiei.dissertation.categoryhandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebDetailsProviderServiceApplication {
    private static final Logger LOGGER = LogManager.getLogger(WebDetailsProviderServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(WebDetailsProviderServiceApplication.class, args);
    }

    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
