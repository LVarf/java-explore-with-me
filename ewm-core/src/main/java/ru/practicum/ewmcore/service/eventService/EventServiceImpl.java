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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventInternalService {
    private final EventRepository repository;
    private final EventShortDtoConverter eventShortDtoConverter;
    private final EventFullDtoConverter eventFullDtoConverter;
    private final EventDtoValidator validator;
    private final EventSpecification specification;

    @Override
    public Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable) {
        final var events = repository.findAllByInitiator(id, pageable);
        return events.map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event) {
        final var eventFromDb = repository.findById(event.getId());
        validator.validationOnExist(eventFromDb.orElse(null));
        validator.validationOnUpdate(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()));
        final var eventFromSave = repository
                .save(eventFullDtoConverter.mergeToEntity(event, eventFromDb.get()));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Page<EventFullDto> readAllEventsByFilters(ClientFilter filters, Pageable pageable) {
        final var eventsFromDb = repository
                .findAll(specification.findAllSpecification(filters), pageable);
        return eventsFromDb.map(eventFullDtoConverter::convertFromEntity);
    }

    public Optional<EventFullDto> createEvent(EventFullDto event) {
        validator.validationOnCreate(event);
        //enrichCategory
        final var eventFromSave = repository
                .save(eventFullDtoConverter.convertToEntity(event));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        validator.validationOnExist(eventFromDb.orElse(null));
        validator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.get()));
        //enrichCategory
        return repository.findById(eventId).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> readEvent(Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        validator.validationOnExist(eventFromDb.orElse(null));
        return eventFromDb.map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        validator.validationOnExist(eventFromDb.orElse(null));
        validator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.get()));
        validator.validationOnCancel(eventFromDb.map(eventFullDtoConverter::convertFromEntity).get());
        final var eventForSave = eventFromDb.get();
        eventForSave.setState(EventStateEnum.CANCELED);
        return Optional.of(eventFullDtoConverter.convertFromEntity(repository.save(eventForSave)));
    }

    @Override
    public Optional<EventFullDto> updateEvent(EventFullDto event) {
        final var eventForSave = eventFullDtoConverter.convertToEntity(event);
        return Optional.of(eventFullDtoConverter.convertFromEntity(repository.save(eventForSave)));
    }

    @Override
    public Optional<EventFullDto> updateEventById(Long eventId, EventFullDto eventFullDto) {
        final var eventFromDb = repository.findById(eventId).orElse(null);
        validator.validationOnExist(eventFromDb);
        final var eventFromSave = repository
                .save(eventFullDtoConverter.mergeToEntity(eventFullDto, eventFromDb));
        return updateEvent(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Optional<EventFullDto> updateEventOnPublish(Long eventId) {
        final var eventFromDb = repository.findById(eventId).orElse(null);
        validator.validationOnExist(eventFromDb);
        validator.validationOnPublished(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.PUBLISHED);
        final var eventFromSave = repository.save(eventFromDb);
        return Optional.of(eventFromSave).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventToReject(Long eventId) {
        final var eventFromDb = repository.findById(eventId).orElse(null);
        validator.validationOnExist(eventFromDb);
        validator.validationOnReject(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.REJECTED);
        final var eventFromSave = repository.save(eventFromDb);
        return Optional.of(eventFromSave).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public List<EventFullDto> readAllByCategoryId(Long catId) {
        final var eventsFromDb = repository.findByCategory(catId);
        return eventsFromDb.stream()
                .map(eventFullDtoConverter::convertFromEntity).collect(Collectors.toList());
    }
}
