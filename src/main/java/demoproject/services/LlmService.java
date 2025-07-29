package demoproject.services;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import demoproject.config.AgentConfig;

/**
 * Service for interacting with Google Gemini Large Language Models
 */
public class LlmService {
    private static final Logger logger = LoggerFactory.getLogger(LlmService.class);
    private final AgentConfig config;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public LlmService(AgentConfig config) {
        this.config = config;
        this.httpClient = HttpClients.createDefault();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Generate code using Google Gemini
     */
    public String generateCode(String prompt) {
        try {
            return generateWithGoogleGemini(prompt);
        } catch (Exception e) {
            logger.error("LLM API call failed: {}", e.getMessage());
            throw new RuntimeException("Google Gemini API call failed", e);
        }
    }

    /**
     * Generate test code using Google Gemini
     */
    public String generateTestCode(String prompt) {
        try {
            return generateWithGoogleGemini(prompt);
        } catch (Exception e) {
            logger.error("LLM API call failed: {}", e.getMessage());
            throw new RuntimeException("Google Gemini API call failed", e);
        }
    }

    /**
     * Generate improvement suggestions using Google Gemini
     */
    public String generateSuggestions(String prompt) {
        try {
            return generateWithGoogleGemini(prompt);
        } catch (Exception e) {
            logger.error("LLM API call failed: {}", e.getMessage());
            throw new RuntimeException("Google Gemini API call failed", e);
        }
    }

    /**
     * Generate code using Google Gemini API
     */
    private String generateWithGoogleGemini(String prompt) throws IOException, JsonProcessingException {
        String apiUrl = config.getLlmApiUrl();
        String apiKey = config.getLlmApiKey();
        String model = config.getLlmModel();

        logger.debug("Making Google Gemini API call to: {}", apiUrl);

        // Build the full URL with API key
        String fullUrl = apiUrl + "?key=" + apiKey;
        HttpPost httpPost = new HttpPost(fullUrl);
        httpPost.setHeader("Content-Type", "application/json");

        // Create the request body according to Google Gemini API format
        ObjectNode requestBody = objectMapper.createObjectNode();
        
        // Add contents array
        ArrayNode contentsArray = objectMapper.createArrayNode();
        ObjectNode content = objectMapper.createObjectNode();
        ArrayNode parts = objectMapper.createArrayNode();
        ObjectNode part = objectMapper.createObjectNode();
        part.put("text", prompt);
        parts.add(part);
        content.set("parts", parts);
        contentsArray.add(content);
        requestBody.set("contents", contentsArray);

        // Add generation config for better control
        ObjectNode generationConfig = objectMapper.createObjectNode();
        generationConfig.put("temperature", 0.2);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.8);
        generationConfig.put("maxOutputTokens", 2048);
        generationConfig.put("candidateCount", 1);
        requestBody.set("generationConfig", generationConfig);

        // Add safety settings
        ArrayNode safetySettings = objectMapper.createArrayNode();
        String[] categories = {"HARM_CATEGORY_HARASSMENT", "HARM_CATEGORY_HATE_SPEECH", 
                             "HARM_CATEGORY_SEXUALLY_EXPLICIT", "HARM_CATEGORY_DANGEROUS_CONTENT"};
        for (String category : categories) {
            ObjectNode safetySetting = objectMapper.createObjectNode();
            safetySetting.put("category", category);
            safetySetting.put("threshold", "BLOCK_MEDIUM_AND_ABOVE");
            safetySettings.add(safetySetting);
        }
        requestBody.set("safetySettings", safetySettings);

        StringEntity entity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        logger.debug("Request body: {}", requestBody.toString());

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String responseBody = EntityUtils.toString(response.getEntity());
            logger.info("Response status: {}, Response body: {}", response.getCode(), responseBody);
            
            if (response.getCode() != 200) {
                logger.error("Google Gemini API call failed with status: {}, response: {}", response.getCode(), responseBody);
                throw new IOException("Google Gemini API call failed with status: " + response.getCode() + ", response: " + responseBody);
            }

            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            
            // Check for error in response
            if (jsonResponse.has("error")) {
                JsonNode error = jsonResponse.get("error");
                String errorMessage = error.path("message").asText("Unknown error");
                logger.error("Google Gemini API returned error: {}", errorMessage);
                throw new IOException("Google Gemini API error: " + errorMessage);
            }

            // Extract the generated text from the response
            JsonNode candidates = jsonResponse.path("candidates");
            logger.info("Candidates found: {}", candidates.size());
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                logger.info("First candidate: {}", firstCandidate.toString());
                
                // Try different response formats
                JsonNode contentResponse = firstCandidate.path("content");
                if (contentResponse.isMissingNode()) {
                    // Try direct text field
                    String directText = firstCandidate.path("text").asText();
                    if (!directText.isEmpty()) {
                        logger.info("Found direct text: {}", directText);
                        return directText;
                    }
                }
                
                logger.info("Content response: {}", contentResponse.toString());
                
                JsonNode responseParts = contentResponse.path("parts");
                logger.info("Response parts: {}", responseParts.toString());
                
                if (responseParts.isArray() && responseParts.size() > 0) {
                    String generatedText = responseParts.get(0).path("text").asText();
                    logger.info("Generated text length: {}", generatedText.length());
                    logger.info("Generated text: {}", generatedText);
                    return generatedText;
                }
                
                // Try alternative path
                String alternativeText = contentResponse.path("text").asText();
                if (!alternativeText.isEmpty()) {
                    logger.info("Found alternative text: {}", alternativeText);
                    return alternativeText;
                }
            }
            
            logger.error("Unexpected response format from Google Gemini API: {}", responseBody);
            throw new IOException("Unexpected response format from Google Gemini API - no candidates found");
        } catch (ParseException e) {
            logger.error("Failed to parse Google Gemini response entity: {}", e.getMessage());
            throw new IOException("Failed to parse Google Gemini response entity", e);
        }
    }



    /**
     * Check if API is configured
     */
    public boolean isApiConfigured() {
                return config.getLlmApiKey() != null
                        && !config.getLlmApiKey().isEmpty()
                && !config.getLlmApiKey().equals("YOUR_GOOGLE_API_KEY_HERE")
                        && config.getLlmApiUrl() != null
                        && !config.getLlmApiUrl().isEmpty();
    }

    /**
     * Test API connection
     */
    public boolean testApiConnection() {
        if (!isApiConfigured()) {
            return false;
        }
        try {
            String testPrompt = "Generate a simple Java method that adds two integers.";
            String response = generateTestCode(testPrompt);
            return response != null && !response.isEmpty();
        } catch (Exception e) {
            logger.error("API connection test failed: {}", e.getMessage());
            return false;
        }
    }
}
