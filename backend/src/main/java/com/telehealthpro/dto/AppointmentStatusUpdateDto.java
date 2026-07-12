package com.telehealthpro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AppointmentStatusUpdateDto {
    @NotBlank(message = "Status is required")
    private String status;
}
