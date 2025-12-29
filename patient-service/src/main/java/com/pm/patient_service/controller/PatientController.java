package com.pm.patient_service.controller;

import com.pm.patient_service.dto.ApiResponse;
import com.pm.patient_service.dto.patients.request.PatientRequestDTO;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.dto.patients.validators.CreatePatientValidationGroup;
import com.pm.patient_service.service.PatientService;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<PatientResponseDTO>>> getAllPatients() {
        List<PatientResponseDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        patients,
                        "Patients retrieved successfully"
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PatientResponseDTO>> getPatientById(@PathVariable UUID id) {
        final PatientResponseDTO patient = patientService.patientById(id);
        return ResponseEntity.ok(
                ApiResponse.success(
                        HttpStatus.OK.value(),
                        patient,
                        "Patient retrieved successfully"
                )
        );
    }

    @PostMapping()
    public  ResponseEntity<ApiResponse<PatientResponseDTO>> savePatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO
    ) {
       final PatientResponseDTO createdPatient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        createdPatient,
                        "Patient created successfully"
                )
        );
    }

    @PatchMapping("/{id}")
    public  ResponseEntity<ApiResponse<PatientResponseDTO>> updatePatient(
            @PathVariable UUID id,
            @Validated({Default.class})
            @RequestBody PatientRequestDTO patientUpdateRequestDTO
    ) {
      final PatientResponseDTO updatedPatient =  patientService.updatePatient(id, patientUpdateRequestDTO);
      return ResponseEntity.ok(
              ApiResponse.success(
                      HttpStatus.OK.value(),
                      updatedPatient,
                      "Patient updated successfully"
              )
      );
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<ApiResponse<String>> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}