package com.pm.patient_service.service;

import com.pm.patient_service.dto.patients.request.PatientRequestDTO;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
     public List<PatientResponseDTO> getAllPatients() {
         final List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
         Patient patient = PatientMapper.toPatient(patientRequestDTO);
         patientRepository.save(patient);
         return PatientMapper.toPatientResponseDTO(patient);
    }
}
