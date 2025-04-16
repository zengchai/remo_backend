package dev.remo.remo.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import dev.remo.remo.Utils.HttpEntityUtils;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/listing")
public class ListingContorller {

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions"; 

    @PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Map<String, String> createListing(@Valid @RequestBody String userMessage) {
        HttpEntity<Map<String, Object>> entity = HttpEntityUtils.buildHttpEntity(userMessage);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<Map> response = restTemplate.exchange(GROQ_API_URL, HttpMethod.POST, entity, Map.class);
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            // Extract response properly with Java 8 compatibility
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty() && choices.get(0).containsKey("message")) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return Collections.singletonMap("response", (String) message.get("content"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonMap("response", "Error: " + e.getMessage());
        }
        return null;
    }
}
