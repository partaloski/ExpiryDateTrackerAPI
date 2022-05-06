package com.example.expirydatetrackerapi;

import com.example.expirydatetrackerapi.helpers.DateParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ExpiryDateTrackerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpiryDateTrackerApiApplication.class, args);
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

}
