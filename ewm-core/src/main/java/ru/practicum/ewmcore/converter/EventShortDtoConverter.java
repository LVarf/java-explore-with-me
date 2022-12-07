package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.stat.ViewStats;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.service.utils.SortConverterMixin;
import ru.practicum.ewmcore.statClient.StatClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventShortDtoConverter implements RootModelConverter<EventShortDto, Event>,
        SortConverterMixin {
    private final TimeUtils timeUtils;
    private final StatClient statClient;
    @Override
    public EventShortDto convertFromEntity(final Event entity) {
        final EventShortDto model = new EventShortDto();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        model.setEventDate(timeUtils.timestampToString(eventDate));
        return model;
    }

    private EventShortDto enrichViews(EventShortDto eventShortDto) {
        final var views = (List<ViewStats>) statClient.getViews(timeUtils
                        .timestampToString(Timestamp.valueOf(LocalDateTime.now().minusYears(2))),
                timeUtils.timestampToString(timeUtils.now()),
                new String[]{"/events" + eventShortDto.getId()}, false);
        for (ViewStats viewStats : views) {
            eventShortDto.setViews(eventShortDto.getViews() + viewStats.getHits());
        }
        return eventShortDto;
    }

    @Override
    public Event convertToEntity(final EventShortDto entity) {
        final Event model = new Event();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        model.setEventDate(timeUtils.stringToTimestamp(eventDate));
        return model;
    }

    @Override
    public Event mergeToEntity(final EventShortDto model, final Event originalEntity) {
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
        return null/*PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                sortUtils.addSecondarySorting(pageable.getSort(),
                        fieldName))*/;
    }
}