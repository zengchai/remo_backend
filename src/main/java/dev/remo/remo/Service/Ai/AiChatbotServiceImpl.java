package dev.remo.remo.Service.Ai;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import dev.remo.remo.Models.Request.PredictPriceRequest;
import dev.remo.remo.Utils.AI.GroqApiUtils;

public class AiChatbotServiceImpl implements AiChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(AiChatbotServiceImpl.class); // Logger for logging
                                                                                              // errors
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    @Autowired
    GroqApiUtils groqApiUtils;

    public String buildPrompt(PredictPriceRequest request) {
        return String.format(
                "Generate a realistic Malaysia market price (in MYR) for a used (second hand) motorcycle based on the following details. "
                        + "Only return the price as a number without any explanation or extra text.\n" +
                        "- Brand: %s\n- Model: %s\n- Manufactured Year: %s\n- Mileage: %s\n- Cubic Capacity: %s\n- Transmission: %s",
                request.getBrand(),
                request.getModel(),
                request.getManufacturedYear(),
                request.getMileage(),
                request.getCubicCapacity(),
                request.getTransmission());
    }

    public String extractFromResponse(String promptMessage) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(API_URL,
                groqApiUtils.sendPromptToAiApi(promptMessage), Map.class);
        ;

        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            return ((String) message.get("content")).replaceAll("[^0-9]", "");
        } catch (Exception e) {
            logger.error("Failed to extract price from API response", e);
        }
        return null;
    }
}
