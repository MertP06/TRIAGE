package com.acil.er_backend.service;

import com.acil.er_backend.dto.DoctorNoteRequest;
import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.AppointmentStatus;
import com.acil.er_backend.model.DoctorNote;
import com.acil.er_backend.repository.AppointmentRepository;
import com.acil.er_backend.repository.DoctorNoteRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DoctorNoteService {

    private final DoctorNoteRepository noteRepo;
    private final AppointmentRepository appointmentRepo;

    public DoctorNoteService(DoctorNoteRepository noteRepo, AppointmentRepository appointmentRepo) {
        this.noteRepo = noteRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Transactional
    public DoctorNote createAndOptionallyComplete(DoctorNoteRequest req, boolean markDone) {
        Appointment ap = appointmentRepo.findById(req.getAppointmentId())
                .orElseThrow(() -> new NoSuchElementException("Randevu bulunamadı: " + req.getAppointmentId()));

        DoctorNote note = new DoctorNote();
        note.setAppointment(ap);
        note.setDiagnosis(req.getDiagnosis());
        note.setSecondaryDiagnosis(req.getSecondaryDiagnosis());
        note.setPlan(req.getPlan());
        note.setPrescription(req.getPrescription());
        note.setLabOrders(req.getLabOrders());
        note.setFollowUpDate(req.getFollowUpDate());
        note.setFollowUpNotes(req.getFollowUpNotes());
        note.setReferralNeeded(req.getReferralNeeded() != null ? req.getReferralNeeded() : false);
        note.setReferralDepartment(req.getReferralDepartment());
        note.setRestDays(req.getRestDays());
        note.setCreatedAt(LocalDateTime.now());

        try {
            note.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            note.setCreatedBy("doctor");
        }

        DoctorNote saved = noteRepo.save(note);

        if (markDone) {
            ap.setStatus(AppointmentStatus.DONE);
            ap.setCompletedAt(LocalDateTime.now());
            appointmentRepo.save(ap);
        }

        return saved;
    }

    public List<DoctorNote> listByAppointment(Long appointmentId) {
        return noteRepo.findByAppointment_IdOrderByCreatedAtDesc(appointmentId);
    }
}
