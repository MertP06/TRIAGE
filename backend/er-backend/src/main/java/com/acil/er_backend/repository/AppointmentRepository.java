package com.acil.er_backend.repository;

import com.acil.er_backend.model.Appointment;
import com.acil.er_backend.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentDateOrderByQueueNumberAsc(LocalDate date);

    List<Appointment> findByAppointmentDateAndStatusOrderByQueueNumberAsc(LocalDate date, AppointmentStatus status);

    @Query("SELECT COALESCE(MAX(a.queueNumber), 0) FROM Appointment a WHERE a.appointmentDate = :date")
    int findTodayMaxQueueNumber(@Param("date") LocalDate date);

    @Query("SELECT a FROM Appointment a WHERE a.patient.tc = :tc AND a.appointmentDate = :date AND a.status IN ('WAITING','CALLED','IN_PROGRESS')")
    Optional<Appointment> findTodayActiveByTc(@Param("tc") String tc, @Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.appointmentDate = :date AND a.status = :status AND a.queueNumber < :queueNumber")
    long countWaitingAhead(@Param("date") LocalDate date, @Param("status") AppointmentStatus status, @Param("queueNumber") Integer queueNumber);

    List<Appointment> findAllByPatientTcOrderByCreatedAtDesc(String tc);
}
