package com.example.webhook.service;

import com.example.webhook.model.GenerateResponse;
import com.example.webhook.entity.ResultEntity;
import com.example.webhook.repository.ResultRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final ResultRepository repo;

    @Value("${app.name}")
    private String name;

    @Value("${app.regNo}")
    private String regNo;

    @Value("${app.email}")
    private String email;

    @Value("${app.generate.url}")
    private String generateUrl;

    @Value("${app.submit.url}")
    private String submitUrl;

    public WebhookService(ResultRepository repo) {
        this.repo = repo;
    }

    public void executeFlowOnStartup() throws Exception {
        System.out.println("🚀 Starting Webhook Flow for " + regNo);

        // Step 1: Generate Webhook
        Map<String, String> requestBody = Map.of(
                "name", name,
                "regNo", regNo,
                "email", email
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(generateUrl, requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            System.err.println("❌ Failed to generate webhook. Status: " + response.getStatusCode());
            return;
        }

        GenerateResponse generateResponse = mapper.readValue(response.getBody(), GenerateResponse.class);
        String webhook = generateResponse.getWebhook();
        String accessToken = generateResponse.getAccessToken();

        System.out.println("✅ Webhook: " + webhook);
        System.out.println("🔑 Access Token received");

        // Step 2: Read solution.sql
        ClassPathResource resource = new ClassPathResource("solution.sql");
        String finalQuery = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8).trim();
        if (finalQuery.isEmpty()) {
            System.err.println("⚠️ solution.sql is empty! Please add your SQL query.");
            return;
        }

        // Step 3: Save query locally
        ResultEntity result = new ResultEntity();
        result.setRegNo(regNo);
        result.setFinalQuery(finalQuery);
        repo.save(result);
        System.out.println("💾 Query saved locally with ID: " + result.getId());

        // Step 4: Submit final query
        HttpHeaders submitHeaders = new HttpHeaders();
        submitHeaders.setContentType(MediaType.APPLICATION_JSON);
        submitHeaders.set("Authorization", accessToken);

        Map<String, String> submitBody = Map.of("finalQuery", finalQuery);
        HttpEntity<Map<String, String>> submitEntity = new HttpEntity<>(submitBody, submitHeaders);

        ResponseEntity<String> submitResponse = restTemplate.postForEntity(submitUrl, submitEntity, String.class);

        System.out.println("📤 Submission Response: " + submitResponse.getStatusCode());
        System.out.println("📦 Response Body: " + submitResponse.getBody());
    }
}
