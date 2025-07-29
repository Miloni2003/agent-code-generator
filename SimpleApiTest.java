import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SimpleApiTest {
    public static void main(String[] args) {
        try {
            System.out.println("=== Simple API Test ===");
            
            String apiKey = "AIzaSyAS1vAAz7FtSvoFj3_HxiQPOoeoIeon0wE";
            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
            
            System.out.println("API Key: " + apiKey.substring(0, 10) + "...");
            System.out.println("API URL: " + apiUrl);
            
            // Create JSON payload
            String jsonPayload = """
                {
                    "contents": [{
                        "parts": [{
                            "text": "Write a simple Java method that adds two integers. Return only the method code."
                        }]
                    }]
                }
                """;
            
            System.out.println("\nSending request...");
            
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Response Status: " + response.statusCode());
            System.out.println("Response Body Length: " + response.body().length());
            System.out.println("Response Body (first 500 chars):");
            System.out.println(response.body().substring(0, Math.min(500, response.body().length())));
            
            if (response.statusCode() == 200) {
                System.out.println("✅ API call successful!");
            } else {
                System.out.println("❌ API call failed with status: " + response.statusCode());
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 