package com.telehealthpro.service;

import com.telehealthpro.dto.DoctorDto;
import com.telehealthpro.dto.DoctorResponseDto;
import com.telehealthpro.entity.Doctor;
import com.telehealthpro.entity.DoctorCategory;
import com.telehealthpro.exception.ResourceNotFoundException;
import com.telehealthpro.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorCategoryService categoryService;

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public Doctor findById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
    }

    public List<Doctor> findByCategory(Long categoryId) {
        return doctorRepository.findByCategoryId(categoryId);
    }

    public List<Doctor> searchByName(String name) {
        return doctorRepository.findByDoctorNameContainingIgnoreCase(name);
    }

    public List<Doctor> findByAvailability(String availability) {
        return doctorRepository.findByAvailability(availability);
    }

    public long countAllDoctors() {
        return doctorRepository.count();
    }

    public DoctorResponseDto toResponseDto(Doctor doctor) {
        return new DoctorResponseDto(
                doctor.getId(),
                doctor.getDoctorName(),
                doctor.getQualification(),
                doctor.getSpecialization(),
                doctor.getExperience(),
                doctor.getHospital(),
                doctor.getConsultationFee(),
                doctor.getRating(),
                doctor.getAvailableDays(),
                doctor.getAvailability(),
                doctor.getPhoto(),
                doctor.getCategory().getId(),
                doctor.getCategory().getCategoryName()
        );
    }

    public List<DoctorResponseDto> toResponseDtoList(List<Doctor> doctors) {
        return doctors.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public Doctor create(DoctorDto dto) {
        Doctor doctor = new Doctor();
        mapDtoToEntity(dto, doctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor update(Long id, DoctorDto dto) {
        Doctor doctor = findById(id);
        mapDtoToEntity(dto, doctor);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = findById(id);
        doctorRepository.delete(doctor);
    }

    private void mapDtoToEntity(DoctorDto dto, Doctor doctor) {
        DoctorCategory category = categoryService.findById(dto.getCategoryId());

        doctor.setDoctorName(dto.getDoctorName());
        doctor.setQualification(dto.getQualification());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setExperience(dto.getExperience());
        doctor.setHospital(dto.getHospital());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setRating(dto.getRating() != null ? dto.getRating() : java.math.BigDecimal.valueOf(4.5));
        doctor.setAvailableDays(dto.getAvailableDays());
        doctor.setAvailability(dto.getAvailability() != null ? dto.getAvailability() : "Available");
        if (dto.getPhoto() != null && !dto.getPhoto().isBlank()) {
            doctor.setPhoto(dto.getPhoto());
        }
        doctor.setCategory(category);
    }
}
