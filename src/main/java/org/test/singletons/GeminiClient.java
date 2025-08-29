package org.test.singletons;

import com.google.genai.Client;
import com.google.genai.types.*;
import io.github.cdimascio.dotenv.Dotenv;

public class GeminiClient {
    private final Client client;
    private static final String AI_MODEL = "gemini-2.5-flash";
    private static final String API_KEY = Dotenv.load().get("GEMINI_API_KEY");
    private final static String INSTRUCTION = "From now on you are a senior dev who can create short and concise, production ready git commit messages (in past tense) given git diff summaries or patches. " +
            "Only the commit message should be returned.";

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
