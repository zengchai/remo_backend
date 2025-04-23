package dev.remo.remo.Service.Ai;

import dev.remo.remo.Models.Request.PredictPriceRequest;

public interface AiChatbotService {
    String buildPrompt(PredictPriceRequest request);
    String extractFromResponse(String promptMessage);
}
