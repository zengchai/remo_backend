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
    String z = String.format(
        "You are an expert in Malaysian used motorcycle pricing. " +
        "Given the following details, predict a fair resale price in MYR for a typical used motorcycle in Malaysia. " +
        "Most used motorcycles in Malaysia are priced between 1,000 and 100,000 MYR. " +
        "Do NOT suggest prices above 100,000 MYR unless the motorcycle is a rare or luxury model (which will be clearly stated). " +
        "Consider the brand, model, year, mileage, engine capacity, and transmission. " +
        "Return ONLY the price as a number, with no currency symbol, explanation, or extra text.\n" +
        "- Brand: %s\n" +
        "- Model: %s\n" +
        "- Manufactured Year: %s\n" +
        "- Mileage (in km): %s\n" +
        "- Engine Capacity (cc): %s\n" +
        "- Transmission Type: %s",
        request.getBrand(),
        request.getModel(),
        request.getManufacturedYear(),
        request.getMileage(),
        request.getCubicCapacity(),
        request.getTransmission()
    );
    logger.info("Generated prompt: {}", z); // Log the generated prompt for debugging
    return z;
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
