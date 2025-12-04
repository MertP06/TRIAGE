package com.acil.er_backend.repository;

import com.acil.er_backend.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    Optional<Patient> findByTc(String tc);
    boolean existsByTc(String tc);
}
