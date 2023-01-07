package ru.practicum.ewmcore.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.repository.CategoryRepository;
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.specification.EventSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.EventDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventInternalService, EventPublicService {
    private static final String SORT_VIEWS = "VIEWS";
    private final EventRepository repository;
    private final EventShortDtoConverter eventShortDtoConverter;
    private final EventFullDtoConverter eventFullDtoConverter;
    private final EventDtoValidator validator;
    private final EventSpecification specification;
    private final TimeUtils timeUtils;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<EventShortDto> readAllEventsPublic(ClientFilter filter, String sort, Pageable pageable) {
        final var specific = specification.findAllSpecificationForPublic(filter, sort);
        final var eventsFromDb = repository.findAll(specific, pageable);
        /*if (sort != null && sort.equals(SORT_VIEWS)) {
            final var events = eventsFromDb.stream()
                    .map(eventShortDtoConverter::convertFromEntity)
                    .sorted((e1, e2) -> (int) (e1.getViews() - e2.getViews()))
                    .collect(Collectors.toList());
            return new PageImpl<EventShortDto>(events, pageable, eventsFromDb.getTotalElements());
        }*/
        return eventsFromDb.map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> readEventPublic(Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        return eventFromDb.filter(event -> event.getState() == EventStateEnum.PUBLISHED)
                .map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable) {
        final var events = repository.findAllByInitiatorId(id, pageable);
        return events.map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<Event> readById(Long eventId) {
        return repository.findById(eventId);
    }

    @Override
    public Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event) {
        final var eventFromDb = repository.findById(event.getEventId());
        validator.validationOnExist(eventFromDb.orElse(null));
        validator.validationOnUpdate(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()));
        final var eventForUpdate = eventFromDb.orElseThrow();
        final var categoryForUpdate = categoryRepository.findById(event.getCategory()).orElseThrow();
        eventForUpdate.setCategory(categoryForUpdate);
        final var eventFromSave = repository.save(eventFullDtoConverter.mergeToEntity(event, eventForUpdate));
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
        event.setCreatedOn(timeUtils.timestampToString(timeUtils.now()));
        final var eventForSave = eventFullDtoConverter.convertToEntity(event);
        eventForSave.setConfirmedRequests(0);
        final var category = categoryRepository.findById(event.getCategory()).orElseThrow();
        eventForSave.setCategory(category);
        eventForSave.setState(EventStateEnum.PENDING);
        final var eventFromSave = repository.save(eventForSave);
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        validator.validationOnExist(eventFromDb.orElse(null));
        validator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.get()));
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
        final var categoryForUpdate = categoryRepository.findById(eventFullDto.getCategory()).orElseThrow();
        eventFromDb.setCategory(categoryForUpdate);
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
        eventFromDb.setPublishedOn(timeUtils.now());
        final var eventFromSave = repository.save(eventFromDb);
        return Optional.of(eventFromSave).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventToReject(Long eventId) {
        final var eventFromDb = repository.findById(eventId).orElse(null);
        validator.validationOnExist(eventFromDb);
        validator.validationOnReject(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.CANCELED);
        final var eventFromSave = repository.save(eventFromDb);
        return Optional.of(eventFromSave).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public List<EventFullDto> readAllByCategoryId(Long catId) {
        final var eventsFromDb = repository.findByCategoryId(catId);
        return eventsFromDb.stream()
                .map(eventFullDtoConverter::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public List<Event> readAllByIds(Set<Long> eventIds) {
        return repository.findAllById(eventIds);
    }
}
