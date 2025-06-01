package com.example.assetrecordingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class AssetRecordingAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AssetRecordingAppApplication.class, args);
    }
}
