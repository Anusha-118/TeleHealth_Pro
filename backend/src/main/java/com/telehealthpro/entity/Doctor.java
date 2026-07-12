package com.telehealthpro.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "doctor_name", nullable = false, length = 100)
    private String doctorName;

    @Column(length = 150)
    private String qualification;

    @Column(length = 100)
    private String specialization;

    @Column(name = "experience_years")
    private Integer experience;

    @Column(length = 150)
    private String hospital;

    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Column(precision = 3, scale = 1)
    private BigDecimal rating;

    @Column(name = "available_days", length = 100)
    private String availableDays;

    @Column(name = "availability", length = 20)
    private String availability = "Available"; // Available / Busy

    @Column(name = "photo", length = 255)
    private String photo = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private DoctorCategory category;
}
