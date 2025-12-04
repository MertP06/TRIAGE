package com.acil.er_backend.config;

import com.acil.er_backend.model.Role;
import com.acil.er_backend.model.User;
import com.acil.er_backend.repository.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeDefaultUsers() {
        if (!userRepository.existsByUsername("nurse")) {
            User nurse = new User();
            nurse.setUsername("nurse");
            nurse.setPassword(passwordEncoder.encode("nurse123"));
            nurse.setFullName("Hemşire Ayşe");
            nurse.setRole(Role.NURSE);
            nurse.setActive(true);
            userRepository.save(nurse);
        }

        if (!userRepository.existsByUsername("doctor")) {
            User doctor = new User();
            doctor.setUsername("doctor");
            doctor.setPassword(passwordEncoder.encode("doctor123"));
            doctor.setFullName("Dr. Mehmet");
            doctor.setRole(Role.DOCTOR);
            doctor.setActive(true);
            userRepository.save(doctor);
        }
    }
}
