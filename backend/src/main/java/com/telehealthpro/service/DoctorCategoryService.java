package com.telehealthpro.service;

import com.telehealthpro.dto.CategoryDto;
import com.telehealthpro.dto.CategoryResponseDto;
import com.telehealthpro.entity.DoctorCategory;
import com.telehealthpro.exception.DuplicateResourceException;
import com.telehealthpro.exception.ResourceNotFoundException;
import com.telehealthpro.repository.DoctorCategoryRepository;
import com.telehealthpro.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorCategoryService {

    private final DoctorCategoryRepository categoryRepository;
    private final DoctorRepository doctorRepository;

    public List<DoctorCategory> findAll() {
        return categoryRepository.findAll();
    }

    public DoctorCategory findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    public long countAvailableDoctors(Long categoryId) {
        return doctorRepository.countByCategoryId(categoryId);
    }

    public long countAllCategories() {
        return categoryRepository.count();
    }

    public CategoryResponseDto toResponseDto(DoctorCategory category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getCategoryName(),
                category.getIconClass(),
                countAvailableDoctors(category.getId())
        );
    }

    public List<CategoryResponseDto> toResponseDtoList(List<DoctorCategory> categories) {
        return categories.stream().map(this::toResponseDto).collect(Collectors.toList());
    }

    @Transactional
    public DoctorCategory create(CategoryDto dto) {
        if (categoryRepository.existsByCategoryNameIgnoreCase(dto.getCategoryName())) {
            throw new DuplicateResourceException("Category already exists: " + dto.getCategoryName());
        }
        DoctorCategory category = new DoctorCategory();
        category.setCategoryName(dto.getCategoryName());
        category.setIconClass(dto.getIconClass() != null ? dto.getIconClass() : "bi-heart-pulse");
        return categoryRepository.save(category);
    }

    @Transactional
    public DoctorCategory update(Long id, CategoryDto dto) {
        DoctorCategory category = findById(id);
        category.setCategoryName(dto.getCategoryName());
        if (dto.getIconClass() != null) {
            category.setIconClass(dto.getIconClass());
        }
        return categoryRepository.save(category);
    }

    @Transactional
    public void delete(Long id) {
        DoctorCategory category = findById(id);
        categoryRepository.delete(category);
    }
}
