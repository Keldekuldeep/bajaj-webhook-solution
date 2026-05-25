package com.kuldeep.webhook.service;

import com.kuldeep.webhook.model.GenerateWebhookRequest;
import com.kuldeep.webhook.model.GenerateWebhookResponse;
import com.kuldeep.webhook.model.SubmitAnswerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService implements ApplicationRunner {

    private static final String GENERATE_WEBHOOK_URL =
            "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    @Autowired
    private SqlSolutionService sqlSolutionService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(ApplicationArguments args) {
        try {
            System.out.println("Starting webhook flow...");

            // Step 1: Generate webhook
            GenerateWebhookResponse webhookResponse = generateWebhook();

            if (webhookResponse == null) {
                System.err.println("Failed to get webhook response. Exiting.");
                return;
            }

            String webhookUrl = webhookResponse.getWebhook();
            String accessToken = webhookResponse.getAccessToken();

            System.out.println("Webhook URL: " + webhookUrl);
            System.out.println("Access Token received: " + (accessToken != null ? "yes" : "no"));

            // Step 2: Get the SQL solution
            String finalQuery = sqlSolutionService.getSqlQuery();
            System.out.println("Final SQL Query: " + finalQuery);

            // Step 3: Submit the answer
            submitAnswer(webhookUrl, accessToken, finalQuery);

        } catch (Exception e) {
            System.err.println("Error during webhook flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private GenerateWebhookResponse generateWebhook() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            GenerateWebhookRequest requestBody = new GenerateWebhookRequest(
                    "Kuldeep Kelde",
                    "0827CI231071",
                    "kuldeepkelde231154@acropolis.in"
            );

            HttpEntity<GenerateWebhookRequest> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<GenerateWebhookResponse> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    entity,
                    GenerateWebhookResponse.class
            );

            System.out.println("Generate webhook status: " + response.getStatusCode());
            return response.getBody();

        } catch (Exception e) {
            System.err.println("Error calling generateWebhook: " + e.getMessage());
            return null;
        }
    }

    private void submitAnswer(String webhookUrl, String accessToken, String finalQuery) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", accessToken);

            SubmitAnswerRequest requestBody = new SubmitAnswerRequest(finalQuery);
            HttpEntity<SubmitAnswerRequest> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    webhookUrl,
                    entity,
                    String.class
            );

            System.out.println("Submit answer status: " + response.getStatusCode());
            System.out.println("Submit answer response: " + response.getBody());

        } catch (Exception e) {
            System.err.println("Error submitting answer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
