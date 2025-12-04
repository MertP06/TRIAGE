package com.acil.er_backend.repository;

import com.acil.er_backend.model.DoctorNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface DoctorNoteRepository extends JpaRepository<DoctorNote, Long> {

    List<DoctorNote> findByAppointment_IdOrderByCreatedAtDesc(Long appointmentId);

    @Query("SELECT d FROM DoctorNote d WHERE d.appointment.patient.tc = :tc ORDER BY d.createdAt DESC")
    List<DoctorNote> findAllByPatientTcOrderByCreatedAtDesc(@Param("tc") String tc);
}
