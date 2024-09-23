package com.example.hotrohoctapbackend.util;

import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import com.convertapi.client.Config;

@Configuration
public class ConvertApiConfig {
    @PostConstruct
    public void init() {
        // Đặt secret cho ConvertAPI
        Config.setDefaultSecret("secret_u5Wttu3yjxTpDbPG");
    }
}
