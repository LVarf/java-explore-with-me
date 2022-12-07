package ru.practicum.ewmcore.service.categoryService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.category.CategoryDto;

import java.util.Optional;

public interface CategoryPublicService {

    Page<CategoryDto> readAllCategoriesPublic(Pageable pageable);

    Optional<CategoryDto> readCategoryPublic(Long catId);
}
