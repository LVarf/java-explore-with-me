package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.location.Location;
import ru.practicum.ewmcore.model.stat.ViewStats;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.statClient.StatClient;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventFullDtoConverter implements RootModelConverter<EventFullDto, Event> {
    private final TimeUtils timeUtils;
    private final StatClient statClient;
    private final UserShortDtoConverter userShortDtoConverter;
    private final CategoryDtoConverter categoryConverter;

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
            model.setCategory(categoryConverter.convertFromEntity(entity.getCategory()));
        }
        model.setEventId(entity.getId());
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
        eventFullDto.setViews(0L);
        final var views = statClient.getViews(start, timeUtils.timestampToString(timeUtils.now()),
                "/events/" + eventFullDto.getId(), false);
        log.info("views[] = {}", Objects.requireNonNull(views));
        for (ViewStats viewStats : views) {
            final var hits = viewStats.getHits() != null ? viewStats.getHits() : 0;
            eventFullDto.setViews(eventFullDto.getViews() + hits);
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
        entity.setAnnotation(model.getAnnotation());
        entity.setDescription(model.getDescription());
        entity.setEventDate(timeUtils.stringToTimestamp(model.getEventDate()));
        entity.setPaid(model.isPaid());
        entity.setParticipantLimit(model.getParticipantLimit());
        entity.setTitle(model.getTitle());
        return entity;
    }
}
