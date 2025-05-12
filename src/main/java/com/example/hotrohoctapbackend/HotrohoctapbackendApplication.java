package com.example.hotrohoctapbackend;

import com.example.hotrohoctapbackend.service.services.PythonScriptService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HotrohoctapbackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HotrohoctapbackendApplication.class, args);
    }
//
//    @Bean
//    public CommandLineRunner run(PythonScriptService pythonScriptService) {
//        return args -> {
//            // Gọi phương thức để chạy script Python khi ứng dụng bắt đầu
//            pythonScriptService.runPythonScript();
//        };
//    }
}
