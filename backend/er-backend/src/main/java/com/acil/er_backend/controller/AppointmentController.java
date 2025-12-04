package com.acil.er_backend.controller;

import com.acil.er_backend.dto.*;
import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.AppointmentStatus;
import com.acil.er_backend.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public List<Appointment> getToday(@RequestParam(required = false) AppointmentStatus status) {
        if (status != null) {
            return appointmentService.getTodayAppointmentsByStatus(status);
        }
        return appointmentService.getTodayAppointments();
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Appointment>> create(@RequestBody Map<String, Object> body) {
        String patientTc = (String) body.get("patientTc");
        String chiefComplaint = (String) body.get("chiefComplaint");
        Integer urgencySelfAssessment = body.get("urgencySelfAssessment") != null
                ? Integer.valueOf(body.get("urgencySelfAssessment").toString()) : null;

        if (patientTc == null || patientTc.isBlank()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("patientTc boş olamaz."));
        }

        try {
            Appointment ap = appointmentService.createAppointment(patientTc, chiefComplaint, urgencySelfAssessment);
            return ResponseEntity.ok(ApiResponse.success("Randevu oluşturuldu.", ap));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Appointment>> updateStatus(
            @PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            AppointmentStatus status = AppointmentStatus.valueOf(body.get("status"));
            Appointment ap = appointmentService.updateStatus(id, status);
            return ResponseEntity.ok(ApiResponse.success("Durum güncellendi.", ap));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<AppointmentDetailResponse> getDetail(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(appointmentService.getDetail(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-patient/{tc}")
    public List<Appointment> getByPatient(@PathVariable String tc) {
        return appointmentService.getAppointmentsByPatientTc(tc);
    }

    @GetMapping("/history/{tc}")
    public ResponseEntity<PatientHistoryResponse> getPatientHistory(@PathVariable String tc) {
        try {
            return ResponseEntity.ok(appointmentService.getPatientHistory(tc));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/dashboard")
    public DashboardStats getDashboard() {
        return appointmentService.getDashboardStats();
    }

    @GetMapping("/waiting-room")
    public WaitingRoomDisplay getWaitingRoom() {
        return appointmentService.getWaitingRoomDisplay();
    }

    @GetMapping("/mobile/queue/{tc}")
    public AppointmentService.MobileQueueStatus getMobileQueue(@PathVariable String tc) {
        return appointmentService.getMobileQueueStatus(tc);
    }
}
