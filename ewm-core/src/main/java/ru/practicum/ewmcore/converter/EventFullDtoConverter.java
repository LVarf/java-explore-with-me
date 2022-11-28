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
    private final TimeUtils timeUtils;

    @Override
    public EventFullDto convertFromEntity(final Event entity) {
        final EventFullDto model = new EventFullDto();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        final var createdOn = entity.getCreatedOn();
        final var publishedOn = entity.getPublishedOn();
        model.setEventDate(timeUtils.timestampToString(eventDate));
        model.setCreatedOn(timeUtils.timestampToString(createdOn));
        model.setPublishedOn(timeUtils.timestampToString(publishedOn));
        model.setCategory(entity.getCategory().getId());
        return model;
    }

    @Override
    public Event convertToEntity(final EventFullDto entity) {
        final Event model = new Event();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        final var createdOn = entity.getCreatedOn();
        final var publishedOn = entity.getPublishedOn();
        model.setEventDate(timeUtils.stringToTimestamp(eventDate));
        model.setCreatedOn(timeUtils.stringToTimestamp(createdOn));
        model.setPublishedOn(timeUtils.stringToTimestamp(publishedOn));
        return model;
    }

    @Override
    public Event mergeToEntity(final EventFullDto model, final Event entity) {
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
