package com.acil.er_backend.service;

import com.acil.er_backend.dto.CreateTriageRequest;
import com.acil.er_backend.model.TriageRecord;
import java.util.List;

public interface TriageService {
    TriageRecord create(CreateTriageRequest req);
    List<TriageRecord> listByAppointment(Long appointmentId);
}
