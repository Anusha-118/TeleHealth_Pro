package com.telehealthpro.repository;

import com.telehealthpro.entity.DoctorCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorCategoryRepository extends JpaRepository<DoctorCategory, Long> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
