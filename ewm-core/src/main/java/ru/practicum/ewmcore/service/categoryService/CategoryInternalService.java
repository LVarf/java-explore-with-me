package ru.practicum.ewmcore.service.categoryService;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.category.CategoryDto;

import java.util.Optional;

@Transactional
public interface CategoryInternalService {

    @Transactional(readOnly = true)
    Optional<CategoryDto> readInternal(Long id);

    Optional<CategoryDto> updateCategoryInternal(CategoryDto categoryDto);

    Optional<CategoryDto> createCategory(CategoryDto categoryDto);

    String deleteCategoryInternal(Long catId);
}
