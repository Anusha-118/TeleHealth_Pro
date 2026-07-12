package com.telehealthpro.controller;

import com.telehealthpro.dto.*;
import com.telehealthpro.service.AppointmentService;
import com.telehealthpro.service.DoctorCategoryService;
import com.telehealthpro.service.DoctorService;
import com.telehealthpro.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminRestController {

    private final UserService userService;
    private final DoctorService doctorService;
    private final DoctorCategoryService categoryService;
    private final AppointmentService appointmentService;

    // ---------------- Dashboard ----------------

    @GetMapping("/stats")
    public AdminStatsResponse stats() {
        return new AdminStatsResponse(
                userService.countAllUsers(),
                doctorService.countAllDoctors(),
                categoryService.countAllCategories(),
                appointmentService.countAll(),
                appointmentService.countToday()
        );
    }

    // ---------------- Doctors ----------------

    @GetMapping("/doctors")
    public List<DoctorResponseDto> listDoctors() {
        return doctorService.toResponseDtoList(doctorService.findAll());
    }

    @PostMapping("/doctors")
    public ResponseEntity<DoctorResponseDto> addDoctor(@Valid @RequestBody DoctorDto dto) {
        var doctor = doctorService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.toResponseDto(doctor));
    }

    @GetMapping("/doctors/{id}")
    public DoctorResponseDto getDoctor(@PathVariable Long id) {
        return doctorService.toResponseDto(doctorService.findById(id));
    }

    @PutMapping("/doctors/{id}")
    public DoctorResponseDto updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorDto dto) {
        return doctorService.toResponseDto(doctorService.update(id, dto));
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<MessageResponse> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Doctor deleted successfully"));
    }

    // ---------------- Categories ----------------

    @GetMapping("/categories")
    public List<CategoryResponseDto> listCategories() {
        return categoryService.toResponseDtoList(categoryService.findAll());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> addCategory(@Valid @RequestBody CategoryDto dto) {
        var category = categoryService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.toResponseDto(category));
    }

    @PutMapping("/categories/{id}")
    public CategoryResponseDto updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto dto) {
        return categoryService.toResponseDto(categoryService.update(id, dto));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }

    // ---------------- Users ----------------

    @GetMapping("/users")
    public List<UserResponseDto> listUsers() {
        return userService.toResponseDtoList(userService.findAllUsers());
    }

    // ---------------- Appointments ----------------

    @GetMapping("/appointments")
    public List<AppointmentResponseDto> listAppointments() {
        return appointmentService.toResponseDtoList(appointmentService.findAll());
    }

    @PutMapping("/appointments/{id}/status")
    public AppointmentResponseDto updateAppointmentStatus(@PathVariable Long id,
                                                            @Valid @RequestBody AppointmentStatusUpdateDto dto) {
        return appointmentService.toResponseDto(appointmentService.updateStatus(id, dto.getStatus()));
    }
}
