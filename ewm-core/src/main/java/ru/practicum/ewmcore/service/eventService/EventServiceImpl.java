package ru.practicum.ewmcore.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.specification.EventSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.EventDtoValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventInternalService {
    private final EventRepository eventRepository;
    private final EventShortDtoConverter eventShortDtoConverter;
    private final EventFullDtoConverter eventFullDtoConverter;
    private final EventDtoValidator eventValidator;
    private final EventSpecification eventSpecification;

    @Override
    public Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable) {
        final var events = eventRepository.findAllByInitiator(id, pageable);
        return events.map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event) {
        final var eventFromDb = eventRepository.findById(event.getId());
        eventValidator.validationOnExist(eventFromDb.orElse(null));
        eventValidator.validationOnUpdate(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()));
        final var eventFromSave = eventRepository
                .save(eventFullDtoConverter.mergeToEntity(event, eventFromDb.get()));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Page<EventFullDto> readAllEventsByFilters(ClientFilter filters, Pageable pageable) {
        final var eventsFromDb = eventRepository
                .findAll(eventSpecification.findAllSpecification(filters), pageable);
        return eventsFromDb.map(eventFullDtoConverter::convertFromEntity);
    }

    public Optional<EventFullDto> createEvent(EventFullDto event) {
        eventValidator.validationOnCreate(event);
        //enrichCategory
        final var eventFromSave = eventRepository
                .save(eventFullDtoConverter.convertToEntity(event));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventValidator.validationOnExist(eventFromDb.orElse(null));
        eventValidator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.get()));
        //enrichCategory
        return eventRepository.findById(eventId).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> readEvent(Long eventId) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventValidator.validationOnExist(eventFromDb.orElse(null));
        return eventFromDb.map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventValidator.validationOnExist(eventFromDb.orElse(null));
        eventValidator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.get()));
        eventValidator.validationOnCancel(eventFromDb.map(eventFullDtoConverter::convertFromEntity).get());
        final var eventForSave = eventFromDb.get();
        eventForSave.setState(EventStateEnum.CANCELED);
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventRepository.save(eventForSave)));
    }

    @Override
    public Optional<EventFullDto> updateEvent(EventFullDto event) {
        final var eventForSave = eventFullDtoConverter.convertToEntity(event);
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventRepository.save(eventForSave)));
    }
}
