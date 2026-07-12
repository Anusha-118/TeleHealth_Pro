package com.telehealthpro.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DoctorDto {

    private Long id;

    @NotBlank(message = "Doctor name is required")
    private String doctorName;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    @NotBlank(message = "Specialization is required")
    private String specialization;

    @NotNull(message = "Experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;

    @NotBlank(message = "Hospital name is required")
    private String hospital;

    @NotNull(message = "Consultation fee is required")
    @DecimalMin(value = "0.0", message = "Fee cannot be negative")
    private BigDecimal consultationFee;

    private BigDecimal rating;

    @NotBlank(message = "Available days are required")
    private String availableDays;

    private String availability;

    private String photo;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
