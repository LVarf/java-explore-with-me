package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.location.Location;
import ru.practicum.ewmcore.model.stat.ViewStats;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.service.utils.SortConverterMixin;
import ru.practicum.ewmcore.statClient.StatClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventFullDtoConverter implements RootModelConverter<EventFullDto, Event>,
        SortConverterMixin {
    private final TimeUtils timeUtils;
    private final StatClient statClient;
    private final UserDtoConverter userDtoConverter;
    private final UserShortDtoConverter userShortDtoConverter;

    @Override
    public EventFullDto convertFromEntity(final Event entity) {
        final EventFullDto model = new EventFullDto();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        final var createdOn = entity.getCreatedOn();
        model.setEventDate(timeUtils.timestampToString(eventDate));
        model.setCreatedOn(timeUtils.timestampToString(createdOn));
        if (entity.getState() == EventStateEnum.PUBLISHED) {
            final var publishedOn = entity.getPublishedOn();
            model.setPublishedOn(timeUtils.timestampToString(publishedOn));
        }
        if (entity.getCategory() != null && entity.getCategory().getId() != null) {
            model.setCategory(entity.getCategory().getId());
        }
        model.setLocation(new Location(entity.getLocationLat(), entity.getLocationLon()));
        model.setInitiator(userShortDtoConverter.convertFromEntity(entity.getInitiator()));
        enrichViews(model);
        return model;
    }

    private EventFullDto enrichViews(EventFullDto eventFullDto) {
        String start = eventFullDto.getPublishedOn();
        if (start == null) {
            start = timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now().minusYears(2)));
        }
        final var views = (List<ViewStats>) statClient.getViews(start, timeUtils.timestampToString(timeUtils.now()),
                new String[]{"/events/" + eventFullDto.getId()}, false).getBody();
        for (ViewStats viewStats : views) {
            eventFullDto.setViews(eventFullDto.getViews() + viewStats.getHits());
        }
        return eventFullDto;
    }

    @Override
    public Event convertToEntity(final EventFullDto entity) {
        final Event model = new Event();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        final var createdOn = entity.getCreatedOn();
        model.setEventDate(timeUtils.stringToTimestamp(eventDate));
        model.setCreatedOn(timeUtils.stringToTimestamp(createdOn));
        model.setInitiator(userShortDtoConverter.convertToEntity(entity.getInitiator()));
        if (entity.getLocation() != null) {
            model.setLocationLat(entity.getLocation().getLat());
            model.setLocationLon(entity.getLocation().getLon());
        }
        if (entity.getState() == EventStateEnum.PUBLISHED) {
            final var publishedOn = entity.getPublishedOn();
            model.setPublishedOn(timeUtils.stringToTimestamp(publishedOn));
        }
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
