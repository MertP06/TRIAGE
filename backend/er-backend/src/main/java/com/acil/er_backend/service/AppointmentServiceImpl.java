package com.acil.er_backend.service;

import com.acil.er_backend.dto.*;
import com.acil.er_backend.model.*;
import com.acil.er_backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final PatientRepository patientRepo;
    private final TriageRecordRepository triageRepo;
    private final DoctorNoteRepository noteRepo;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepo, PatientRepository patientRepo,
            TriageRecordRepository triageRepo, DoctorNoteRepository noteRepo) {
        this.appointmentRepo = appointmentRepo;
        this.patientRepo = patientRepo;
        this.triageRepo = triageRepo;
        this.noteRepo = noteRepo;
    }

    @Override
    @Transactional
    public Appointment createAppointment(String patientTc, String chiefComplaint, Integer urgencySelfAssessment) {
        Patient patient = patientRepo.findByTc(patientTc)
                .orElseThrow(() -> new NoSuchElementException("Hasta bulunamadı: " + patientTc));

        int nextQueue = appointmentRepo.findTodayMaxQueueNumber(LocalDate.now()) + 1;
        int waitingCount = getTodayAppointmentsByStatus(AppointmentStatus.WAITING).size();

        Appointment ap = new Appointment();
        ap.setPatient(patient);
        ap.setQueueNumber(nextQueue);
        ap.setAppointmentDate(LocalDate.now());
        ap.setStatus(AppointmentStatus.WAITING);
        ap.setChiefComplaint(chiefComplaint);
        ap.setUrgencySelfAssessment(urgencySelfAssessment);
        ap.setEstimatedWaitMinutes(waitingCount * 15);
        ap.setCreatedAt(LocalDateTime.now());

        return appointmentRepo.save(ap);
    }

    @Override
    public List<Appointment> getTodayAppointments() {
        return appointmentRepo.findByAppointmentDateOrderByQueueNumberAsc(LocalDate.now());
    }

    @Override
    public List<Appointment> getTodayAppointmentsByStatus(AppointmentStatus status) {
        return appointmentRepo.findByAppointmentDateAndStatusOrderByQueueNumberAsc(LocalDate.now(), status);
    }

    @Override
    @Transactional
    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment ap = appointmentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Randevu bulunamadı: " + id));

        ap.setStatus(status);
        if (status == AppointmentStatus.CALLED) {
            ap.setCalledAt(LocalDateTime.now());
        } else if (status == AppointmentStatus.IN_PROGRESS) {
            ap.setStartedAt(LocalDateTime.now());
        } else if (status == AppointmentStatus.DONE || status == AppointmentStatus.NO_SHOW) {
            ap.setCompletedAt(LocalDateTime.now());
        }

        return appointmentRepo.save(ap);
    }

    @Override
    public AppointmentDetailResponse getDetail(Long id) {
        Appointment ap = appointmentRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Randevu bulunamadı: " + id));

        AppointmentDetailResponse resp = new AppointmentDetailResponse();
        resp.setAppointment(ap);
        resp.setPatient(ap.getPatient());
        resp.setTriageRecords(triageRepo.findByAppointment_IdOrderByCreatedAtDesc(id));
        resp.setDoctorNotes(noteRepo.findByAppointment_IdOrderByCreatedAtDesc(id));

        return resp;
    }

    @Override
    public List<Appointment> getAppointmentsByPatientTc(String tc) {
        return appointmentRepo.findAllByPatientTcOrderByCreatedAtDesc(tc);
    }

    @Override
    public PatientHistoryResponse getPatientHistory(String tc) {
        Patient patient = patientRepo.findByTc(tc)
                .orElseThrow(() -> new NoSuchElementException("Hasta bulunamadı: " + tc));

        PatientHistoryResponse resp = new PatientHistoryResponse();
        resp.setPatient(patient);
        resp.setAppointments(appointmentRepo.findAllByPatientTcOrderByCreatedAtDesc(tc));
        resp.setTriageRecords(triageRepo.findAllByPatientTcOrderByCreatedAtDesc(tc));
        resp.setDoctorNotes(noteRepo.findAllByPatientTcOrderByCreatedAtDesc(tc));

        return resp;
    }

    @Override
    public DashboardStats getDashboardStats() {
        List<Appointment> today = getTodayAppointments();

        DashboardStats stats = new DashboardStats();
        stats.setTotalToday(today.size());
        stats.setWaiting((int) today.stream().filter(a -> a.getStatus() == AppointmentStatus.WAITING).count());
        stats.setCalled((int) today.stream().filter(a -> a.getStatus() == AppointmentStatus.CALLED).count());
        stats.setInProgress((int) today.stream().filter(a -> a.getStatus() == AppointmentStatus.IN_PROGRESS).count());
        stats.setDone((int) today.stream().filter(a -> a.getStatus() == AppointmentStatus.DONE).count());
        stats.setNoShow((int) today.stream().filter(a -> a.getStatus() == AppointmentStatus.NO_SHOW).count());

        Map<String, Integer> levels = new HashMap<>();
        List<TriageRecord> triages = new ArrayList<>();
        for (Appointment ap : today) {
            triages.addAll(triageRepo.findByAppointment_IdOrderByCreatedAtDesc(ap.getId()));
        }
        for (TriageRecord tr : triages) {
            String level = tr.getTriageLevel() != null ? tr.getTriageLevel() : "BELIRSIZ";
            levels.put(level, levels.getOrDefault(level, 0) + 1);
        }
        stats.setTriageLevels(levels);

        double totalWait = 0;
        int waitCount = 0;
        for (Appointment ap : today) {
            Long wait = ap.getActualWaitMinutes();
            if (wait != null) {
                totalWait += wait;
                waitCount++;
            }
        }
        stats.setAvgWaitTime(waitCount > 0 ? totalWait / waitCount : null);

        return stats;
    }

    @Override
    public WaitingRoomDisplay getWaitingRoomDisplay() {
        WaitingRoomDisplay display = new WaitingRoomDisplay();
        LocalDate today = LocalDate.now();

        List<Appointment> called = getTodayAppointmentsByStatus(AppointmentStatus.CALLED);
        if (!called.isEmpty()) {
            Appointment current = called.get(0);
            WaitingRoomDisplay.CurrentCall call = new WaitingRoomDisplay.CurrentCall();
            call.setQueueNumber(current.getQueueNumber());
            String name = current.getPatient().getName();
            String[] parts = name.split(" ");
            String maskedName = parts[0] + " " + (parts.length > 1 ? parts[parts.length - 1].charAt(0) + "***" : "");
            call.setPatientName(maskedName);
            call.setMessage("Lütfen muayene odasına geçiniz");
            display.setCurrentCall(call);
        }

        List<Appointment> waiting = getTodayAppointmentsByStatus(AppointmentStatus.WAITING);
        List<WaitingRoomDisplay.WaitingPatient> list = new ArrayList<>();
        for (int i = 0; i < waiting.size(); i++) {
            Appointment ap = waiting.get(i);
            WaitingRoomDisplay.WaitingPatient wp = new WaitingRoomDisplay.WaitingPatient();
            wp.setQueueNumber(ap.getQueueNumber());
            wp.setStatus("Bekliyor");
            wp.setAheadCount(i);
            list.add(wp);
        }
        display.setWaitingList(list);
        display.setTotalWaiting(waiting.size());
        display.setLastUpdated(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        return display;
    }

    @Override
    public MobileQueueStatus getMobileQueueStatus(String tc) {
        MobileQueueStatus status = new MobileQueueStatus();

        Optional<Appointment> opt = appointmentRepo.findTodayActiveByTc(tc, LocalDate.now());
        if (opt.isEmpty()) {
            status.setFound(false);
            status.setMessage("Bugün için aktif randevunuz bulunmamaktadır.");
            return status;
        }

        Appointment ap = opt.get();
        status.setFound(true);
        status.setQueueNumber(ap.getQueueNumber());
        status.setStatus(ap.getStatus().name());
        status.setPatientName(ap.getPatient().getName());
        status.setEstimatedWaitMinutes(ap.getEstimatedWaitMinutes());

        if (ap.getStatus() == AppointmentStatus.CALLED) {
            status.setWaitingAhead(0);
            status.setMessage("Sıranız geldi! Lütfen muayene odasına geçiniz.");
        } else if (ap.getStatus() == AppointmentStatus.IN_PROGRESS) {
            status.setWaitingAhead(0);
            status.setMessage("Muayeneniz devam ediyor.");
        } else {
            long ahead = appointmentRepo.countWaitingAhead(LocalDate.now(), AppointmentStatus.WAITING, ap.getQueueNumber());
            status.setWaitingAhead((int) ahead);
            status.setMessage("Sıranızı bekliyorsunuz. Önünüzde " + ahead + " kişi var.");
        }

        return status;
    }
}
