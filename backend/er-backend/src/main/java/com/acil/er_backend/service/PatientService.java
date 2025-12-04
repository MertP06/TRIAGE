package com.acil.er_backend.service;

import com.acil.er_backend.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientByTc(String tc);
    void deletePatient(String tc);
    boolean existsByTc(String tc);
    Patient updatePatient(String tc, Patient updated);
    Patient partialUpdatePatient(String tc, Patient patch);
}
