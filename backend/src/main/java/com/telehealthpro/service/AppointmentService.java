package com.telehealthpro.service;

import com.telehealthpro.dto.AppointmentDto;
import com.telehealthpro.dto.AppointmentResponseDto;
import com.telehealthpro.entity.Appointment;
import com.telehealthpro.entity.Doctor;
import com.telehealthpro.entity.User;
import com.telehealthpro.exception.ResourceNotFoundException;
import com.telehealthpro.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;

    @Transactional
    public Appointment bookAppointment(AppointmentDto dto, User user) {
        Doctor doctor = doctorService.findById(dto.getDoctorId());

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setDoctor(doctor);
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus("Pending");

        return appointmentRepository.save(appointment);
    }

    public List<Appointment> findByUser(Long userId) {
        return appointmentRepository.findByUserId(userId);
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public Appointment findById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + id));
    }

    @Transactional
    public Appointment updateStatus(Long id, String status) {
        Appointment appointment = findById(id);
        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }

    public long countAll() {
        return appointmentRepository.count();
    }

    public long countToday() {
        return appointmentRepository.countByAppointmentDate(LocalDate.now());
    }

    public long countByStatus(String status) {
        return appointmentRepository.countByStatus(status);
    }

    public AppointmentResponseDto toResponseDto(Appointment appointment) {
        return new AppointmentResponseDto(
                appointment.getId(),
                appointment.getAppointmentDate(),
                appointment.getAppointmentTime(),
                appointment.getStatus(),
                appointment.getDoctor().getId(),
                appointment.getDoctor().getDoctorName(),
                appointment.getDoctor().getSpecialization(),
                appointment.getUser().getId(),
                appointment.getUser().getFullName()
        );
    }

    public List<AppointmentResponseDto> toResponseDtoList(List<Appointment> appointments) {
        return appointments.stream().map(this::toResponseDto).collect(Collectors.toList());
    }
}
