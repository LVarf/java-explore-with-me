package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventValidator {
    private final static String CATEGORY_NOT_FOUND = "Categories not found";
    private CategoryInternalService categoryInternalService;
    public void validateOnRead(long[] categories, ValidationMode validationMode)
            throws ValidationException {
        final var apiError = new ApiError();
        validateCategoriesExist(categories, apiError);
    }

    private void validateCategoriesExist(long[] categories, ApiError apiError) {
        final List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (long id : categories) {
            final var category = categoryInternalService.readInternal(id).orElse(null);
            if (category != null) {
                categoryDtoList.add(category);
            }
        }
        if (categoryDtoList.isEmpty()) {
            log.info("Categories id: {} not found", Arrays.toString(categories));
            apiError.setMessage("Categories are not found");
            throw apiError;
        }
    }
}
