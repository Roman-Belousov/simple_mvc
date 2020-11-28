package org.example.web.config;

import org.example.app.services.IdProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
@ComponentScan(basePackages = "org.example.app")
public class AppContextConfig {

    @Bean(name = "IdProvider")
    @Scope("prototype")
    public IdProvider idProvider(){
        IdProvider idProvider = new IdProvider();
        return idProvider;

    }
}
