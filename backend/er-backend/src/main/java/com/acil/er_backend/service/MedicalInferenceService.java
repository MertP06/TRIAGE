package com.acil.er_backend.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalInferenceService {

    private final MedicalDataService medicalDataService;

    public MedicalInferenceService(MedicalDataService medicalDataService) {
        this.medicalDataService = medicalDataService;
    }

    public List<Map<String, Object>> suggestTop5(List<String> symptoms) {
        if (symptoms == null || symptoms.isEmpty()) return List.of();

        List<Map<String, Object>> matches = medicalDataService.searchBySymptoms(symptoms);

        List<Map<String, Object>> scored = new ArrayList<>();
        for (Map<String, Object> rec : matches) {
            Object symptomsObj = rec.get("symptoms");
            int matchCount = 0;
            if (symptomsObj instanceof List<?> list) {
                for (Object s : list) {
                    if (s != null) {
                        String lower = s.toString().toLowerCase().trim();
                        for (String input : symptoms) {
                            if (lower.equals(input.toLowerCase().trim())) {
                                matchCount++;
                                break;
                            }
                        }
                    }
                }
            }
            Map<String, Object> copy = new HashMap<>(rec);
            copy.put("match_score", matchCount);
            scored.add(copy);
        }

        return scored.stream()
                .sorted((a, b) -> {
                    int scoreA = (int) a.getOrDefault("match_score", 0);
                    int scoreB = (int) b.getOrDefault("match_score", 0);
                    if (scoreB != scoreA) return scoreB - scoreA;
                    Object uA = a.get("urgency_level");
                    Object uB = b.get("urgency_level");
                    int urgA = uA != null ? Integer.parseInt(uA.toString()) : 0;
                    int urgB = uB != null ? Integer.parseInt(uB.toString()) : 0;
                    return urgB - urgA;
                })
                .limit(5)
                .collect(Collectors.toList());
    }
}
