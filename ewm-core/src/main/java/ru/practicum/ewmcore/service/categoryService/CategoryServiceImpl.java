package ru.practicum.ewmcore.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CategoryDtoConverter;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.repository.CategoryRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.validator.CategoryValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryInternalService, CategoryPublicService {
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
    public List<CategoryDto> readAllCategoriesPublic(Pageable pageable) {
        return repository.findAll(pageable).map(converter::convertFromEntity).toList();
    }

    @Override
    public Optional<CategoryDto> readCategoryPublic(Long catId) {
        final var categoryFromDb = repository.findById(catId).map(converter::convertFromEntity);
        validator.validationOnResponse(categoryFromDb.orElse(null));
        return categoryFromDb;
    }

    @Override
    public Optional<CategoryDto> updateCategoryInternal(CategoryDto categoryDto) {
        validator.validationCategoryOnUpdate(categoryDto);
        final var categoryFromDb = repository.findById(categoryDto.getId()).orElse(null);
        validator.validationOnExist(converter.convertFromEntity(categoryFromDb));
        final var isUnique = !categoryDto.getName().equals(categoryFromDb.getName()) &&
                repository.findCategoryByName(categoryDto.getName()).orElse(null) != null;
        validator.validationUniqueName(isUnique);
        validator.validationNameCategory(categoryDto);
        final var categoryFromSave = repository.save(converter.mergeToEntity(categoryDto, categoryFromDb));
        return Optional.of(categoryFromSave).map(converter::convertFromEntity);
    }

    @Override
    public Optional<CategoryDto> createCategory(CategoryDto categoryDto) {
        validator.validationCategoryOnSave(categoryDto);
        final var isUnique = repository.findCategoryByName(categoryDto.getName()).orElse(null) != null;
        validator.validationUniqueName(isUnique);
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
