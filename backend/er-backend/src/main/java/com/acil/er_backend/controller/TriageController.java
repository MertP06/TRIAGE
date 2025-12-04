package com.acil.er_backend.controller;

import com.acil.er_backend.dto.ApiResponse;
import com.acil.er_backend.dto.CreateTriageRequest;
import com.acil.er_backend.model.TriageRecord;
import com.acil.er_backend.service.TriageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/triage")
public class TriageController {

    private final TriageService triageService;

    public TriageController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TriageRecord>> create(@Valid @RequestBody CreateTriageRequest request) {
        try {
            TriageRecord record = triageService.create(request);
            return ResponseEntity.ok(ApiResponse.success("Triaj kaydı oluşturuldu.", record));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public List<TriageRecord> getByAppointment(@PathVariable Long appointmentId) {
        return triageService.listByAppointment(appointmentId);
    }
}
