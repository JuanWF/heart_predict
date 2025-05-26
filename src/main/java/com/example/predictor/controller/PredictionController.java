package com.example.predictor.controller;

import com.example.predictor.dto.PredictionResponse;
import com.example.predictor.model.PatientData;
import com.example.predictor.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/predict")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping
    public PredictionResponse predict(@RequestBody PatientData data) {
        String prediction = predictionService.predict(data);

        String suggestion;
        try {
            suggestion = predictionService.getGeminiSuggestion(prediction, data);
        } catch (Exception e) {
            suggestion = "No se pudo obtener una sugerencia del modelo Gemini.";
        }

        List<String> stats = predictionService.generateStatistics(data);

        return new PredictionResponse(prediction, suggestion, stats);
    }
}
