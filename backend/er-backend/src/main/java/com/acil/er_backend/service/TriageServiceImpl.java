package com.acil.er_backend.service;

import com.acil.er_backend.dto.CreateTriageRequest;
import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.TriageRecord;
import com.acil.er_backend.repository.AppointmentRepository;
import com.acil.er_backend.repository.TriageRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TriageServiceImpl implements TriageService {

    private final AppointmentRepository appointmentRepository;
    private final TriageRecordRepository triageRecordRepository;
    private final MedicalInferenceService inferenceService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TriageServiceImpl(AppointmentRepository appointmentRepository,
            TriageRecordRepository triageRecordRepository,
            MedicalInferenceService inferenceService) {
        this.appointmentRepository = appointmentRepository;
        this.triageRecordRepository = triageRecordRepository;
        this.inferenceService = inferenceService;
    }

    @Override
    @Transactional
    public TriageRecord create(CreateTriageRequest req) {
        Appointment ap = appointmentRepository.findById(req.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı: " + req.getAppointmentId()));

        TriageRecord tr = new TriageRecord();
        tr.setAppointment(ap);
        tr.setNurseSymptomsCsv(req.getNurseSymptomsCsv());
        tr.setTemperature(req.getTemperature());
        tr.setPulse(req.getPulse());
        tr.setBpHigh(req.getBpHigh());
        tr.setBpLow(req.getBpLow());
        tr.setOxygenSaturation(req.getOxygenSaturation());
        tr.setRespiratoryRate(req.getRespiratoryRate());
        tr.setPainLevel(req.getPainLevel());
        tr.setBloodGlucose(req.getBloodGlucose());
        tr.setTriageLevel(req.getTriageLevel());
        tr.setNotes(req.getNotes());
        tr.setCreatedAt(LocalDateTime.now());

        try {
            tr.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            tr.setCreatedBy("system");
        }

        List<String> symptoms = parseCsv(req.getNurseSymptomsCsv());
        try {
            var suggestions = inferenceService.suggestTop5(symptoms);
            if (suggestions != null && !suggestions.isEmpty()) {
                tr.setSuggestionsJson(objectMapper.writeValueAsString(suggestions));
                int maxUrgency = suggestions.stream()
                        .mapToInt(s -> {
                            Object level = ((Map<?, ?>) s).get("urgency_level");
                            return level != null ? Integer.parseInt(level.toString()) : 0;
                        })
                        .max().orElse(0);
                tr.setAiSuggestedLevel(maxUrgency >= 4 ? "KIRMIZI" : maxUrgency >= 3 ? "SARI" : "YESIL");
                tr.setAiConfidence((int) Math.min(100, 50 + suggestions.size() * 10));
            }
        } catch (Exception ignored) {}

        return triageRecordRepository.save(tr);
    }

    @Override
    public List<TriageRecord> listByAppointment(Long appointmentId) {
        return triageRecordRepository.findByAppointment_IdOrderByCreatedAtDesc(appointmentId);
    }

    private List<String> parseCsv(String csv) {
        if (csv == null || csv.isBlank()) return List.of();
        return Arrays.stream(csv.split(",")).map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toList());
    }
}
