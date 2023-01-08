package ru.practicum.ewmcore.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.category.Category;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
public class CategoryDtoConverter implements RootModelConverter<CategoryDto, Category> {
    @Override
    public CategoryDto convertFromEntity(final Category entity) {
        final CategoryDto model = new CategoryDto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public Category convertToEntity(final CategoryDto entity) {
        final Category model = new Category();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public Category mergeToEntity(final CategoryDto model, final Category entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
