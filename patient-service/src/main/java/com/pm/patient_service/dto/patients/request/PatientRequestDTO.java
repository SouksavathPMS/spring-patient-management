package com.pm.patient_service.dto.patients.request;


import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PatientRequestDTO {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is required")
    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be yyyy-MM-dd format")
    @Size(min = 10, max = 10, message = "Date of birth must be exactly 10 characters")
    private String dateOfBirth;

    @NotBlank(message = "Registered date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Registered date must be yyyy-MM-dd format")
    private String registeredDate;
}

