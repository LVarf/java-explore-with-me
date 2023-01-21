package ru.practicum.ewmcore.service.categoryService;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.category.CategoryDto;

import java.util.List;
import java.util.Optional;

public interface CategoryPublicService {

    List<CategoryDto> readAllCategoriesPublic(Pageable pageable);

    Optional<CategoryDto> readCategoryPublic(Long catId);
}
