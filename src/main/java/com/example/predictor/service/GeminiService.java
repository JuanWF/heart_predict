package com.example.predictor.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public String getSuggestion(String predictionText, String patientSummary) {
        try {
            String prompt = "Soy un sistema médico experto. El resultado de la predicción es: "
                    + predictionText
                    + ". Datos del paciente: " + patientSummary
                    + ". Dame una sugerencia clínica o recomendación profesional médica para este caso, como si fuera un cardiólogo.";

            String requestBody = """
                    {
                    "contents": [
                        {
                        "parts": [
                            {
                            "text": "%s"
                            }
                        ]
                        }
                    ]
                    }
                    """.formatted(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            // System.out.println("Respuesta completa del API Gemini:");
            // System.out.println(responseBody);

            // Parsear usando org.json
            JSONObject json = new JSONObject(responseBody);
            JSONArray candidates = json.getJSONArray("candidates");
            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            String suggestion = parts.getJSONObject(0).getString("text");

            return suggestion;

        } catch (Exception e) {
            e.printStackTrace();
            return "No se pudo obtener una sugerencia del modelo Gemini.";
        }
    }

}

