package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.stat.ViewStats;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.statClient.StatClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EventShortDtoConverter implements RootModelConverter<EventShortDto, Event> {
    private final TimeUtils timeUtils;
    private final StatClient statClient;

    @Override
    public EventShortDto convertFromEntity(final Event entity) {
        final EventShortDto model = new EventShortDto();
        BeanUtils.copyProperties(entity, model);
        final var eventDate = entity.getEventDate();
        model.setEventDate(timeUtils.timestampToString(eventDate));
        enrichViews(model);
        return model;
    }

    private EventShortDto enrichViews(EventShortDto eventShortDto) {
        String start = timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now().minusYears(2)));
        eventShortDto.setViews(0L);
        final var views = statClient.getViews(start, timeUtils.timestampToString(timeUtils.now()),
                "/events/" + eventShortDto.getId(), false);
        for (ViewStats viewStats : views) {
            final var hits = viewStats.getHits() != null ? viewStats.getHits() : 0;
            eventShortDto.setViews(eventShortDto.getViews() + hits);
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
}