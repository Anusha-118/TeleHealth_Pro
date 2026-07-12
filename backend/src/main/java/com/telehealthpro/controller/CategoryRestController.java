package com.telehealthpro.controller;

import com.telehealthpro.dto.CategoryResponseDto;
import com.telehealthpro.service.DoctorCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final DoctorCategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDto> listCategories() {
        return categoryService.toResponseDtoList(categoryService.findAll());
    }
}
