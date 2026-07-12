package com.telehealthpro.controller;

import com.telehealthpro.dto.DoctorResponseDto;
import com.telehealthpro.entity.Doctor;
import com.telehealthpro.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public read-only endpoints for browsing doctors.
 * Booking lives in AppointmentRestController since it requires authentication.
 */
@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;

    @GetMapping
    public List<DoctorResponseDto> listDoctors(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "availability", required = false) String availability) {

        List<Doctor> doctors;
        if (name != null && !name.isBlank()) {
            doctors = doctorService.searchByName(name);
        } else if (categoryId != null) {
            doctors = doctorService.findByCategory(categoryId);
        } else if (availability != null && !availability.isBlank()) {
            doctors = doctorService.findByAvailability(availability);
        } else {
            doctors = doctorService.findAll();
        }
        return doctorService.toResponseDtoList(doctors);
    }

    @GetMapping("/{id}")
    public DoctorResponseDto getDoctor(@PathVariable Long id) {
        return doctorService.toResponseDto(doctorService.findById(id));
    }
}
