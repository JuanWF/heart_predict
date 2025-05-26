package com.example.predictor.controller;

import com.example.predictor.model.PatientData;
import com.example.predictor.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping
    public Map<String, String> predict(@RequestBody PatientData data) {
        Map<String, String> response = new HashMap<>();

        // Obtener predicción del modelo Weka
        String prediction = predictionService.predict(data);
        response.put("prediction", prediction);

        try {
            // Llamada al API Gemini y análisis de la sugerencia
            String suggestion = predictionService.getGeminiSuggestion(prediction, data);
            response.put("suggestion", suggestion);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("suggestion", "No se pudo obtener una sugerencia del modelo Gemini.");
        }

        return response;
    }
}
