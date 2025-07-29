package demoproject.services;

import demoproject.config.AgentConfig;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SimpleLlmService {
    
    private final AgentConfig config;
    private final HttpClient httpClient;
    
    public SimpleLlmService(AgentConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newHttpClient();
    }
    
    public String generateTestCode(String prompt) throws IOException, InterruptedException {
        System.out.println("Sending request to LLM...");
        
        String jsonPayload = """
            {
                "contents": [{
                    "parts": [{
                        "text": "%s"
                    }]
                }]
            }
            """.formatted(prompt.replace("\"", "\\\"").replace("\n", "\\n"));
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(config.getLlmApiUrl() + "?key=" + config.getLlmApiKey()))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
            .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("API call failed with status: " + response.statusCode() + " - " + response.body());
        }
        
        // Parse the response to extract the text content
        String responseBody = response.body();
        String textContent = extractTextFromResponse(responseBody);
        
        System.out.println("Received response from LLM (" + textContent.length() + " characters)");
        return textContent;
    }
    
    private String extractTextFromResponse(String responseBody) {
        // Simple extraction - look for "text": "..." pattern
        int textIndex = responseBody.indexOf("\"text\":");
        if (textIndex == -1) {
            return responseBody; // Return full response if we can't parse it
        }
        
        int startQuote = responseBody.indexOf("\"", textIndex + 7);
        if (startQuote == -1) {
            return responseBody;
        }
        
        int endQuote = responseBody.indexOf("\"", startQuote + 1);
        if (endQuote == -1) {
            return responseBody;
        }
        
        return responseBody.substring(startQuote + 1, endQuote)
                         .replace("\\n", "\n")
                         .replace("\\\"", "\"");
    }
} 