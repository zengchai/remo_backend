package dev.remo.remo.Utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class GroqApiUtils {

    private static final String API_KEY = "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";

    public HttpEntity<Map<String,Object>> sendPromptToAiApi(String userMessage) {

        // Build headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0");

        // Create message body
        Map<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);

        List<Map<String, Object>> messagesList = new ArrayList<>();
        messagesList.add(userMessageMap);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "meta-llama/llama-4-scout-17b-16e-instruct");
        requestBody.put("messages", messagesList);

        return new HttpEntity<>(requestBody, headers);
    }

}
