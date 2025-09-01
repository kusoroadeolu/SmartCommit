package org.test.singletons;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import org.test.FileUtils;

/**
 * Gemini wrapper class that abstracts most of gemini's features
 * */
public class GeminiClient {
    private final Client client;
    private static final String AI_MODEL = "gemini-2.5-flash";
    private static final String API_KEY = FileUtils.extractGeminiToken();
    private final static String INSTRUCTION = "Based on the given git diff, write a commit message in imperative mood that follows Git best practices:\n" +
            "\n" +
            "Requirements:\n" +
            "- Use imperative mood (e.g. \"Add\", \"Fix\", \"Update\", not \"Added\", \"Fixed\", \"Updated\")\n" +
            "- Keep subject line under 50 characters\n" +
            "- If the change is complex, include a body explaining why\n" +
            "- Make it atomic - describe the single logical change being made\n" +
            "\n" +
            "Format:\n" +
            "Subject line\n" +
            "\n" +
            "Optional body with more details if needed\n" +
            "\n" +
            "Return only the commit message, no additional text or explanation.";

    public static GeminiClient getInstance(){
        return GeminiClientHelper.CLIENT;
    }


    //Generates summary(less detailed) messages from the DiffSummary class.
    //This is better for larger git repos or repos with more changes
    public String generateMessage(String snippets){
        Content content = Content.fromParts(
                Part.fromText(snippets)
        );
        return generateMessage(content);
    }

    private GeminiClient(){
        this.client = Client
                .builder()
                .apiKey(API_KEY)
                .build();
    }

    private static GenerateContentConfig config(){
        Content systemInstruction = Content.fromParts(
                Part.fromText(INSTRUCTION)
        );

        return GenerateContentConfig
                .builder()
                .systemInstruction(systemInstruction)
                .build();
    }

    private String generateMessage(Content content){
        GenerateContentResponse response = client.models.generateContent(
                AI_MODEL, content, config()
        );
        return response.text();
    }


    private static class GeminiClientHelper{
        private static final GeminiClient CLIENT = new GeminiClient();
    }

}
