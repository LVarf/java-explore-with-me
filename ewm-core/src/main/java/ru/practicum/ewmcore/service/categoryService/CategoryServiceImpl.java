package ru.practicum.ewmcore.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CategoryDtoConverter;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.repository.CategoryRepository;
import ru.practicum.ewmcore.validator.CategoryValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryInternalService {
    private final CategoryRepository repository;
    private final CategoryDtoConverter converter;
    private final CategoryValidator validator;
    @Override
    public Optional<CategoryDto> readInternal(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<CategoryDto> updateCategoryInternal(CategoryDto categoryDto) {
        final var categoryFromDb = repository.findById(categoryDto.getId()).orElse(null);
        validator.validationOnExist(converter.convertFromEntity(categoryFromDb));
        validator.validationNameCategory(categoryDto);
        final var categoryFromSave = repository.save(converter.mergeToEntity(categoryDto, categoryFromDb));
        return Optional.of(categoryFromSave).map(converter::convertFromEntity);
    }

}
