package ru.practicum.ewmcore.converter;

//public class EventFullDtoConverter {

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.service.utils.SortConverterMixin;

@Component
@RequiredArgsConstructor
public class EventFullDtoConverter implements RootModelConverter<EventFullDto, Event>,
        SortConverterMixin {

    @Override
    public EventFullDto convertFromEntity(final Event entity) {
        final EventFullDto model = new EventFullDto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public Event convertToEntity(final EventFullDto model) {
        final Event entity = new Event();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

    @Override
    public Event mergeToEntity(final EventFullDto model, final Event originalEntity) {
        final Event entity = new Event();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

    @Override
    public Sort.Order convertSortPropertyToEntityStyle(Sort.Order order) {
        final String property = order.getProperty();
        return order;
    }

    @Override
    public Pageable secondarySort(Pageable pageable, String fieldName) {
        return null /*PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sortUtils.addSecondarySorting(pageable.getSort(),
                        fieldName))*/;
    }
}
