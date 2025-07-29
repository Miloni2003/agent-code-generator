package demoproject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TestHttpSimple {
    public static void main(String[] args) {
        try {
            System.out.println("=== Simple HTTP Test for Google Gemini ===");
            
            String apiKey = "AIzaSyDiCinm73irvEr1SA7vBfiN9ifuVkwuQtk";
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + apiKey;
            
            String jsonBody = """
                {
                  "contents": [
                    {
                      "parts": [
                        {
                          "text": "Write a simple Java method that adds two integers. Return only the code."
                        }
                      ]
                    }
                  ],
                  "generationConfig": {
                    "temperature": 0.2,
                    "maxOutputTokens": 2048
                  }
                }
                """;
            
            HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
            
            System.out.println("Sending request to: " + url);
            System.out.println("Request body: " + jsonBody);
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("\n=== RESPONSE ===");
            System.out.println("Status: " + response.statusCode());
            System.out.println("Body: " + response.body());
            System.out.println("=== END RESPONSE ===");
            
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 