package dev.remo.remo.Utils.AI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GroqApiUtils {

    @Value("${groq.api.secret}")
    private String api;

    public HttpEntity<Map<String, Object>> sendPromptToAiApi(String userMessage) {

        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(api);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0");

        // Create message body
        Map<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);

        List<Map<String, Object>> messagesList = new ArrayList<>();
        messagesList.add(userMessageMap);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gemma2-9b-it");
        requestBody.put("messages", messagesList);

        return new HttpEntity<>(requestBody, headers);
    }

}
