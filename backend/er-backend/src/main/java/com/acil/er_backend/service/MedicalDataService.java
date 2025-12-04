package com.acil.er_backend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MedicalDataService {

    private List<Map<String, Object>> records = List.of();
    private Set<String> allSymptoms = Set.of();

    @PostConstruct
    public void init() {
        try {
            InputStream is = new ClassPathResource("medical_data.json").getInputStream();
            ObjectMapper om = new ObjectMapper();
            this.records = om.readValue(is, new TypeReference<>() {});

            Set<String> syms = new HashSet<>();
            for (Map<String, Object> rec : records) {
                Object symptomsObj = rec.get("symptoms");
                if (symptomsObj instanceof List<?> list) {
                    for (Object s : list) {
                        if (s != null) syms.add(s.toString().toLowerCase().trim());
                    }
                }
            }
            this.allSymptoms = syms;
        } catch (Exception e) {
            this.records = List.of();
            this.allSymptoms = Set.of();
        }
    }

    public List<Map<String, Object>> getRecords() {
        return records;
    }

    public Set<String> getAllSymptoms() {
        return allSymptoms;
    }

    public List<Map<String, Object>> searchBySymptoms(List<String> symptoms) {
        if (symptoms == null || symptoms.isEmpty()) return List.of();

        Set<String> lower = symptoms.stream()
                .map(s -> s.toLowerCase().trim())
                .collect(Collectors.toSet());

        return records.stream()
                .filter(rec -> {
                    Object symptomsObj = rec.get("symptoms");
                    if (symptomsObj instanceof List<?> list) {
                        for (Object s : list) {
                            if (s != null && lower.contains(s.toString().toLowerCase().trim())) {
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}
