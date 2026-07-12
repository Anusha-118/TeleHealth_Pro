package com.telehealthpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseDto {
    private Long id;
    private String doctorName;
    private String qualification;
    private String specialization;
    private Integer experience;
    private String hospital;
    private BigDecimal consultationFee;
    private BigDecimal rating;
    private String availableDays;
    private String availability;
    private String photo;
    private Long categoryId;
    private String categoryName;
}
