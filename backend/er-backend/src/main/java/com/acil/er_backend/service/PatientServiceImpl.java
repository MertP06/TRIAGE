package com.acil.er_backend.service;

import com.acil.er_backend.model.Patient;
import com.acil.er_backend.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientByTc(String tc) {
        return patientRepository.findByTc(tc);
    }

    @Override
    public void deletePatient(String tc) {
        patientRepository.deleteById(tc);
    }

    @Override
    public boolean existsByTc(String tc) {
        return patientRepository.existsByTc(tc);
    }

    @Override
    public Patient updatePatient(String tc, Patient updated) {
        Patient existing = patientRepository.findByTc(tc)
                .orElseThrow(() -> new NoSuchElementException("Hasta bulunamadı: " + tc));
        existing.setName(updated.getName());
        if (updated.getBirthYear() != null) existing.setBirthYear(updated.getBirthYear());
        if (updated.getGender() != null) existing.setGender(updated.getGender());
        return patientRepository.save(existing);
    }

    @Override
    public Patient partialUpdatePatient(String tc, Patient patch) {
        Patient existing = patientRepository.findByTc(tc)
                .orElseThrow(() -> new NoSuchElementException("Hasta bulunamadı: " + tc));
        if (patch.getName() != null) existing.setName(patch.getName());
        if (patch.getBirthYear() != null) existing.setBirthYear(patch.getBirthYear());
        if (patch.getGender() != null) existing.setGender(patch.getGender());
        return patientRepository.save(existing);
    }
}
