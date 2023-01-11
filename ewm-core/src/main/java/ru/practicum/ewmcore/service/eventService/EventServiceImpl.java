package ru.practicum.ewmcore.service.eventService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public List<EventShortDto> readAllEventsPublic(ClientFilter filter, String sort, Pageable pageable) {
        final var eventsFromDb = repository.findAll(specification.findAllSpecificationForPublic(filter), pageable);
        if (sort != null && sort.equals(SORT_VIEWS)) {
            final var events = eventsFromDb.stream()
                    .map(eventShortDtoConverter::convertFromEntity)
                    .sorted((e1, e2) -> (int) (e1.getViews() - e2.getViews()))
                    .collect(Collectors.toList());
            return new PageImpl<EventShortDto>(events, pageable, eventsFromDb.getTotalElements()).toList();
        }
        return eventsFromDb.map(eventShortDtoConverter::convertFromEntity).toList();
    }

    @Override
    public Optional<EventFullDto> readEventPublic(Long eventId) {
        return repository.findById(eventId).filter(event -> event.getState() == EventStateEnum.PUBLISHED)
                .map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable) {
        return repository.findAllByInitiatorId(id, pageable).map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<Event> readById(Long eventId) {
        return readEventImpl(eventId);
    }

    @Override
    public Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event) {
        if (event.getId() != null) {
            event.setEventId(event.getId());
        }
        final var eventFromDb = readEventImpl(event.getEventId());
        validator.validationOnUpdate(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()));
        final var eventForUpdate = eventFromDb.orElseThrow();
        final var categoryForUpdate = categoryRepository.findById(event.getCategory().getId()).orElseThrow();
        eventForUpdate.setCategory(categoryForUpdate);
        final var eventFromSave = repository.save(eventFullDtoConverter.mergeToEntity(event, eventForUpdate));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    @Override
    public Page<EventFullDto> readAllEventsByFilters(ClientFilter filters, Pageable pageable) {
        return repository.findAll(specification.findAllSpecification(filters), pageable)
                .map(eventFullDtoConverter::convertFromEntity);
    }

    public Optional<EventFullDto> createEvent(EventFullDto event) {
        validator.validationOnCreate(event);
        event.setCreatedOn(timeUtils.timestampToString(timeUtils.now()));
        final var eventForSave = eventFullDtoConverter.convertToEntity(event);
        eventForSave.setConfirmedRequests(0);
        final var category = categoryRepository.findById(event.getCategory().getId()).orElseThrow();
        eventForSave.setCategory(category);
        eventForSave.setState(EventStateEnum.PENDING);
        return Optional.of(eventFullDtoConverter.convertFromEntity(repository.save(eventForSave)));
    }

    @Override
    public Optional<EventFullDto> readEventByInitiator(Long userId, Long eventId) {
        final var eventFromDb = readEventImpl(eventId);
        validator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()));
        return repository.findById(eventId).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> readEvent(Long eventId) {
        return readEventImpl(eventId).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId) {
        final var eventFromDb = readEventImpl(eventId).orElseThrow();
        validator.validationOnRead(userId, eventFullDtoConverter.convertFromEntity(eventFromDb));
        validator.validationOnCancel(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.CANCELED);
        return Optional.of(eventFullDtoConverter.convertFromEntity(repository.save(eventFromDb)));
    }

    @Override
    public Optional<Event> updateEvent(Event event) {
        return Optional.of(repository.save(event));
    }

    @Override
    public Optional<EventFullDto> updateEventById(Long eventId, EventFullDto eventFullDto) {
        final var eventFromDb = readEventImpl(eventId).orElseThrow();
        categoryRepository.findById(eventFullDto.getCategory().getId()).ifPresent(eventFromDb::setCategory);
        final var eventFromSave = updateEvent(eventFullDtoConverter.mergeToEntity(eventFullDto, eventFromDb));
        return eventFromSave.map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventOnPublish(Long eventId) {
        final var eventFromDb = readEventImpl(eventId).orElseThrow();
        validator.validationOnPublished(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.PUBLISHED);
        eventFromDb.setPublishedOn(timeUtils.now());
        return Optional.of(repository.save(eventFromDb)).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventToReject(Long eventId) {
        final var eventFromDb = readEventImpl(eventId).orElseThrow();
        validator.validationOnReject(eventFullDtoConverter.convertFromEntity(eventFromDb));
        eventFromDb.setState(EventStateEnum.CANCELED);
        return Optional.of(repository.save(eventFromDb)).map(eventFullDtoConverter::convertFromEntity);
    }

    @Override
    public List<EventFullDto> readAllByCategoryId(Long catId) {
        return repository.findByCategoryId(catId).stream()
                .map(eventFullDtoConverter::convertFromEntity).collect(Collectors.toList());
    }

    private Optional<Event> readEventImpl(Long eventId) {
        final var eventFromDb = repository.findById(eventId);
        validator.validationOnExist(eventFromDb.orElse(null));
        return eventFromDb;
    }

    @Override
    public List<Event> readAllByIds(Set<Long> eventIds) {
        return repository.findAllById(eventIds);
    }
}
