package com.example.webhook;

import com.example.webhook.service.WebhookService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebhookApp {

    public static void main(String[] args) {
        SpringApplication.run(WebhookApp.class, args);
    }

    @Bean
    public ApplicationRunner startup(WebhookService webhookService) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                webhookService.executeFlowOnStartup();
            }
        };
    }
}
