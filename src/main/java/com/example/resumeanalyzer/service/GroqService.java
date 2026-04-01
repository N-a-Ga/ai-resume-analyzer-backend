package com.example.resumeanalyzer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final ObjectMapper mapper = new ObjectMapper();

    public String analyzeResume(String prompt) {

        try {
            String url = "https://api.groq.com/openai/v1/chat/completions";

            RestTemplate restTemplate = new RestTemplate();

            // 🔥 CLEAN PROMPT (FIX)
            String safePrompt = prompt
                    .replace("\"", "\\\"")
                    .replace("\n", " ")
                    .replace("\r", " ");

            String requestBody = """
            {
              "model": "llama-3.1-8b-instant",
              "messages": [
                {
                  "role": "user",
                  "content": "%s"
                }
              ]
            }
            """.formatted(safePrompt);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, entity, String.class);

            JsonNode root = mapper.readTree(response.getBody());

            String content = root
                    .path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

            return content;

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Groq Error: " + e.getMessage();
        }
    }
}