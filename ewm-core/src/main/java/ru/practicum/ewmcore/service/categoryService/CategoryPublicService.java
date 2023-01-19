package ru.practicum.ewmcore.service.categoryService;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.category.CategoryDto;

import java.util.List;
import java.util.Optional;

@Transactional
public interface CategoryPublicService {

    @Transactional(readOnly = true)

    List<CategoryDto> readAllCategoriesPublic(Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CategoryDto> readCategoryPublic(Long catId);
}
