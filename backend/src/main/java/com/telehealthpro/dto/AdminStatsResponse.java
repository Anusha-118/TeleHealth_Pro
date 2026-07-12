package com.telehealthpro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsResponse {
    private long totalUsers;
    private long totalDoctors;
    private long totalCategories;
    private long totalAppointments;
    private long todaysAppointments;
}
