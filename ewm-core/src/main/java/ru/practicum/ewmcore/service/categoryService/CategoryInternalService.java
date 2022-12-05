package ru.practicum.ewmcore.service.categoryService;

import ru.practicum.ewmcore.model.category.CategoryDto;

import java.util.Optional;

public interface CategoryInternalService {
    Optional<CategoryDto> readInternal(Long id);

    Optional<CategoryDto> updateCategoryInternal(CategoryDto categoryDto);

    Optional<CategoryDto> createCategory(CategoryDto categoryDto);
}
