package com.telehealthpro.repository;

import com.telehealthpro.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByCategoryId(Long categoryId);

    List<Doctor> findByDoctorNameContainingIgnoreCase(String name);

    List<Doctor> findByAvailability(String availability);

    long countByCategoryId(Long categoryId);
}
