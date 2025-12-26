package com.pm.patient_service.controller;

import com.pm.patient_service.dto.ApiResponse;
import com.pm.patient_service.dto.patients.request.PatientRequestDTO;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(
                ApiResponse.success(patients, "Patients retrieved successfully")
        );
    }

    @PostMapping()
    public  ResponseEntity<ApiResponse<PatientResponseDTO>> savePatient(
            @Valid @RequestBody PatientRequestDTO patientRequestDTO
    ) {
       final PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok(
                ApiResponse.success(createdPatient, "Patient created successfully")
        );
    }
}