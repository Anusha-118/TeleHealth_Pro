package com.telehealthpro.controller;

import com.telehealthpro.dto.AppointmentDto;
import com.telehealthpro.dto.AppointmentResponseDto;
import com.telehealthpro.security.UserPrincipal;
import com.telehealthpro.service.AppointmentService;
import com.telehealthpro.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentRestController {

    private final AppointmentService appointmentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> bookAppointment(
            @Valid @RequestBody AppointmentDto dto,
            @AuthenticationPrincipal UserPrincipal principal) {

        var appointment = appointmentService.bookAppointment(dto, userService.findById(principal.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentService.toResponseDto(appointment));
    }

    @GetMapping("/my")
    public List<AppointmentResponseDto> myAppointments(@AuthenticationPrincipal UserPrincipal principal) {
        return appointmentService.toResponseDtoList(appointmentService.findByUser(principal.getId()));
    }
}
