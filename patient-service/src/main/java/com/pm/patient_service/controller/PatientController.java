package com.pm.patient_service.controller;

import com.pm.patient_service.dto.ApiResponse;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}