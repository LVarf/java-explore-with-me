package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventFullDtoResponse;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class EventFullDtoResponseConverter implements RootModelConverter<EventFullDtoResponse, EventFullDto> {
    @Override
    public EventFullDtoResponse convertFromEntity(final EventFullDto entity) {
        final EventFullDtoResponse model = new EventFullDtoResponse();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public EventFullDto convertToEntity(final EventFullDtoResponse entity) {
        final EventFullDto model = new EventFullDto();
        BeanUtils.copyProperties(entity, model);
        model.setCategory(new CategoryDto().setId(entity.getCategory()));
        return model;
    }

    @Override
    public EventFullDto mergeToEntity(final EventFullDtoResponse model, final EventFullDto originalEntity) {
        final EventFullDto entity = new EventFullDto();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}