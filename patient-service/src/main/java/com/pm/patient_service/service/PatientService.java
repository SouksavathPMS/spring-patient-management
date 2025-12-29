package com.pm.patient_service.service;

import com.pm.patient_service.dto.patients.request.PatientRequestDTO;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.exception.DuplicateResourceException;
import com.pm.patient_service.exception.EmailNotFoundException;
import com.pm.patient_service.exception.PatientNotFoundException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
     public List<PatientResponseDTO> getAllPatients() {
         final List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
    }

    public PatientResponseDTO patientById(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient with id " + id + " not found")
        );
        return PatientMapper.toPatientResponseDTO(patient);
    }
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        // Check if email already exists
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new DuplicateResourceException("A patient with " + patientRequestDTO.getEmail() + "'s email already exists");
        }
         Patient patient = PatientMapper.toPatient(patientRequestDTO);
         patientRepository.save(patient);
         return PatientMapper.toPatientResponseDTO(patient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {

         // find patient by given id
         Patient patient = patientRepository.findById(id).orElseThrow(
                 () -> new  PatientNotFoundException("Patient with id " + id + " not found")
         );

        // Check if email already exists
        if (!patientRepository.existsByEmail(patient.getEmail())) {
            throw new EmailNotFoundException("A patient with " + patient.getEmail() + "'s email not found");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toPatientResponseDTO(updatedPatient);
    }

    public void deletePatient(UUID id) {
         Patient patient = patientRepository.findById(id).orElseThrow(
                 () -> new PatientNotFoundException("Patient with id " + id + " not found")
         );
         patientRepository.delete(patient);
    }
}
