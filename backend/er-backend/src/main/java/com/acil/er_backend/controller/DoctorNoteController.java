package com.acil.er_backend.controller;

import com.acil.er_backend.dto.ApiResponse;
import com.acil.er_backend.dto.DoctorNoteRequest;
import com.acil.er_backend.model.DoctorNote;
import com.acil.er_backend.service.DoctorNoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/doctor-notes")
public class DoctorNoteController {

    private final DoctorNoteService doctorNoteService;

    public DoctorNoteController(DoctorNoteService doctorNoteService) {
        this.doctorNoteService = doctorNoteService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DoctorNote>> create(
            @Valid @RequestBody DoctorNoteRequest request,
            @RequestParam(defaultValue = "false") boolean markDone) {
        try {
            DoctorNote note = doctorNoteService.createAndOptionallyComplete(request, markDone);
            return ResponseEntity.ok(ApiResponse.success("Doktor notu kaydedildi.", note));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/by-appointment/{appointmentId}")
    public List<DoctorNote> getByAppointment(@PathVariable Long appointmentId) {
        return doctorNoteService.listByAppointment(appointmentId);
    }
}
