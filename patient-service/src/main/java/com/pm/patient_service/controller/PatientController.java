package com.pm.patient_service.controller;

import com.pm.patient_service.dto.ApiResponse;
import com.pm.patient_service.dto.patients.request.PatientRequestDTO;
import com.pm.patient_service.dto.patients.response.PatientResponseDTO;
import com.pm.patient_service.dto.patients.validators.CreatePatientValidationGroup;
import com.pm.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
@Tag(name = "Patients", description = "API for managing Patients")
public class PatientController {

    private final PatientService patientService;

    @Operation(
            summary = "Get all Patients",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Patients retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"status": 200,
                                                    	"message": "Patients retrieved successfully",
                                                    	"data": [
                                                    		{
                                                    			"id": "123e4567-e89b-12d3-a456-426614174000",
                                                    			"name": "John Doe",
                                                    			"email": "john.doe@example.com",
                                                    			"address": "123 Main St, Springfield",
                                                    			"dateOfBirth": "1985-06-15"
                                                    		}
                                                    	]
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"status": 500,
                                                    	"message": "Internal server error"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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


    @Operation(
            summary = "Get Patient by UUID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Patient retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"status": 200,
                                                    	"message": "Patients retrieved successfully",
                                                    	"data": {
                                                    			"id": "123e4567-e89b-12d3-a456-426614174000",
                                                    			"name": "John Doe",
                                                    			"email": "john.doe@example.com",
                                                    			"address": "123 Main St, Springfield",
                                                    			"dateOfBirth": "1985-06-15"
                                                    	}
                                                    }
                                                    """
                                    )
                            )

                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Patient not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                     	"status": 404,
                                                     	"message": "Patient with id 123e4567-e89b-12d3-a456-000000000004 not found"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Create Patient",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "201",
                            description = "Patient created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"status": 201,
                                                    	"message": "Patients created successfully",
                                                    	"data": {
                                                    			"id": "123e4567-e89b-12d3-a456-426614174000",
                                                    			"name": "John Doe",
                                                    			"email": "john.doe@example.com",
                                                    			"address": "123 Main St, Springfield",
                                                    			"dateOfBirth": "1985-06-15"
                                                    	}
                                                    }
                                                    """
                                    )
                            )

                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation Failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                     	"status": 400,
                                                     	"message": "Validation failed",
                                                     	"errors": {
                                                     		"name": "Name is required"
                                                     	}
                                                     }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Update Patient by given UUID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Patient updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                    	"status": 201,
                                                    	"message": "Patients created successfully",
                                                    	"data": {
                                                    			"id": "123e4567-e89b-12d3-a456-426614174000",
                                                    			"name": "John Doe",
                                                    			"email": "john.doe@example.com",
                                                    			"address": "123 Main St, Springfield",
                                                    			"dateOfBirth": "1985-06-15"
                                                    	}
                                                    }
                                                    """
                                    )
                            )

                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Validation Failed",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                    {
                                                     	"status": 400,
                                                     	"message": "Validation failed",
                                                     	"errors": {
                                                     		"name": "Name is required"
                                                     	}
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Delete Patient by UUID",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "204",
                            description = "Patient deleted successfully (no content returned)"
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Patient not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponse.class),
                                    examples = @ExampleObject(
                                            value = """
                                                {
                                                    "status": 404,
                                                    "message": "Patient with id 123e4567-e89b-12d3-a456-000000000004 not found"
                                                }
                                                """
                                    )
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}