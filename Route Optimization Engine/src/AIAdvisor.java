// AIAdvisor.java
// Calls Google Gemini API (FREE tier) for natural language travel advisories.
// Uses Java's built-in HttpURLConnection — no external libraries needed.

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AIAdvisor {

    // Paste your Gemini API key here (free at aistudio.google.com)
    private static final String API_KEY = "API key";
    private static final String API_URL =
            "https://generativelanguage.googleapis.com/v1beta/models/" +
                    "gemini-2.0-flash:generateContent?key=" + API_KEY;

    private static final boolean AI_ENABLED =
            !API_KEY.equals("API key");

    // --- PUBLIC: getAdvisory() ---
    public String getAdvisory(RouteResult result) {
        if (!AI_ENABLED) {
            return getFallbackAdvisory(result);
        }
        try {
            return callGeminiAPI(result);
        } catch (Exception e) {
            return "[AI unavailable: " + e.getMessage() + "]\n"
                    + getFallbackAdvisory(result);
        }
    }

    // --- PRIVATE: callGeminiAPI() ---
    private String callGeminiAPI(RouteResult result) throws Exception {

        String prompt = "You are a smart Chennai urban transit advisor. "
                + "A commuter is taking this route: "
                + result.getPathAsString()
                + ". Total travel time: " + result.getTotalTime()
                + " minutes with " + result.getStopCount()
                + " intermediate stops in Chennai, India. "
                + "Give a concise helpful travel advisory in 3 sentences max. "
                + "Include one practical tip. Be friendly and direct.";

        // Build Gemini JSON request body
        String requestBody = "{"
                + "\"contents\": [{"
                + "  \"parts\": [{\"text\": \""
                + prompt.replace("\"", "\\\"")
                + "\"}]"
                + "}],"
                + "\"generationConfig\": {"
                + "  \"maxOutputTokens\": 200,"
                + "  \"temperature\": 0.7"
                + "}"
                + "}";

        // Set up HTTP connection
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(15000);

        // Send request
        OutputStream os = conn.getOutputStream();
        os.write(requestBody.getBytes("UTF-8"));
        os.close();

        // Read response
        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        // Extract text from Gemini JSON response
        // Response shape: {"candidates":[{"content":{"parts":[{"text":"..."}]}}]}
        String json = response.toString();
        String marker = "\"text\": \"";
        int start = json.indexOf(marker);
        if (start == -1) return "Could not parse AI response.";
        start += marker.length();
        int end = json.indexOf("\"", start);
        if (end == -1) return "Could not parse AI response.";

        return json.substring(start, end)
                .replace("\\n", "\n")
                .replace("\\\"", "\"");
    }

    // --- PRIVATE: getFallbackAdvisory() ---
    // Used when no API key is set — app still works perfectly
    private String getFallbackAdvisory(RouteResult result) {
        StringBuilder advisory = new StringBuilder();
        advisory.append("  📋 Travel Advisory:\n");

        if (result.getTotalTime() <= 20) {
            advisory.append("  ✔ Short journey — direct and efficient route.\n");
        } else if (result.getTotalTime() <= 40) {
            advisory.append("  ✔ Moderate journey — allow extra 10 mins during peak hours.\n");
        } else {
            advisory.append("  ⚠ Long journey — consider travelling before 8AM or after 7PM.\n");
        }

        if (result.getStopCount() == 0) {
            advisory.append("  ✔ Direct route — no transfers needed.\n");
        } else if (result.getStopCount() == 1) {
            advisory.append("  ✔ One transfer — allow 5 minutes at the interchange.\n");
        } else {
            advisory.append("  ⚠ Multiple transfers — keep your journey plan handy.\n");
        }

        advisory.append("  💡 Tip: Validate your ticket at every boarding point.\n");
        advisory.append("\n  [Add Gemini API key in AIAdvisor.java to enable AI advisories]");

        return advisory.toString();
    }
}