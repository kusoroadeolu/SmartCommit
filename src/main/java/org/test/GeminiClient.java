package org.test;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;

public class GeminiClient {
    private final Client client;
    private static final String AI_MODEL = "gemini-2.5-flash";
    private static final String API_KEY = Dotenv.load().get("GEMINI_API_KEY");

    public static GeminiClient getGeminiClient(){
        return GeminiClientHelper.CLIENT;
    }

    protected GeminiClient(){
        this.client = Client
                .builder()
                .apiKey(API_KEY)
                .build();
    }

    private static class GeminiClientHelper{
        private static final GeminiClient CLIENT = new GeminiClient();
    }

    public Client getClient() {
        return this.client;
    }
}
