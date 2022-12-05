package ru.practicum.ewmcore.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CategoryDtoConverter;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.repository.CategoryRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.validator.CategoryValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryInternalService {
    private static final String CATEGORY_IS_DELETED = "Категория удалена";
    private static final String CATEGORY_IS_NOT_DELETED = "Категория не удалена";
    private final CategoryRepository repository;
    private final CategoryDtoConverter converter;
    private final CategoryValidator validator;
    private final EventInternalService eventService;
    @Override
    public Optional<CategoryDto> readInternal(Long id) {
        return repository.findById(id).map(converter::convertFromEntity);
    }

    @Override
    public Optional<CategoryDto> updateCategoryInternal(CategoryDto categoryDto) {
        final var categoryFromDb = repository.findById(categoryDto.getId()).orElse(null);
        validator.validationOnExist(converter.convertFromEntity(categoryFromDb));
        validator.validationNameCategory(categoryDto);
        final var categoryFromSave = repository.save(converter.mergeToEntity(categoryDto, categoryFromDb));
        return Optional.of(categoryFromSave).map(converter::convertFromEntity);
    }

    @Override
    public Optional<CategoryDto> createCategory(CategoryDto categoryDto) {
        validator.validationNameCategory(categoryDto);
        final var categoryFromSave = repository.save(converter.convertToEntity(categoryDto));
        return Optional.of(categoryFromSave).map(converter::convertFromEntity);
    }

    @Override
    public String deleteCategoryInternal(Long catId) {
        final var categoryFromDb = repository.findById(catId).orElse(null);
        validator.validationOnExist(converter.convertFromEntity(categoryFromDb));
        final var events = eventService.readAllByCategoryId(catId);
        if (events.isEmpty()) {
            repository.delete(categoryFromDb);
            return CATEGORY_IS_DELETED;
        }
        return CATEGORY_IS_NOT_DELETED;
    }
}
