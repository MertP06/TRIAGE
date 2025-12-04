package com.acil.er_backend.controller;

import com.acil.er_backend.dto.ApiResponse;
import com.acil.er_backend.model.Patient;
import com.acil.er_backend.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAll() {
        return patientService.getAllPatients();
    }

    @GetMapping("/{tc}")
    public ResponseEntity<Patient> getByTc(@PathVariable String tc) {
        return patientService.getPatientByTc(tc)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Patient>> create(@Valid @RequestBody Patient patient) {
        if (patientService.existsByTc(patient.getTc())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Bu TC ile kayıtlı hasta zaten mevcut."));
        }
        Patient saved = patientService.savePatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Hasta kaydedildi.", saved));
    }

    @PutMapping("/{tc}")
    public ResponseEntity<Patient> update(@PathVariable String tc, @Valid @RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(patientService.updatePatient(tc, patient));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{tc}")
    public ResponseEntity<Patient> partialUpdate(@PathVariable String tc, @RequestBody Patient patient) {
        try {
            return ResponseEntity.ok(patientService.partialUpdatePatient(tc, patient));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{tc}")
    public ResponseEntity<Void> delete(@PathVariable String tc) {
        patientService.deletePatient(tc);
        return ResponseEntity.noContent().build();
    }
}
