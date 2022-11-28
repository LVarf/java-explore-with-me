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
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.validator.EventDtoValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventInternalServiceImpl implements EventInternalService {
    private final EventRepository eventRepository;
    private final EventShortDtoConverter eventShortDtoConverter;
    private final EventFullDtoConverter eventFullDtoConverter;
    private final EventDtoValidator eventValidator;

    @Override
    public Page<EventShortDto> readAllByInitiatorId(Long id, Pageable pageable) {
        final var events = eventRepository.findAllByInitiator(id, pageable);
        return events.map(eventShortDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<EventFullDto> updateEventByUser(Long userId, EventFullDto event) {
        final var eventFromDb = eventRepository.findById(event.getId());
        eventValidator.validationOnExist(eventFromDb.orElse(null));
        eventValidator.validationOnUpdate(eventFullDtoConverter.convertFromEntity(eventFromDb.orElseThrow()), userId);
        final var eventFromSave = eventRepository
                .save(eventFullDtoConverter.mergeToEntity(event, eventFromDb.get()));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }

    public Optional<EventFullDto> createEvent(EventFullDto event) {
        eventValidator.validationOnCreate(event);
        //enrichCategory
        final var eventFromSave = eventRepository
                .save(eventFullDtoConverter.convertToEntity(event));
        return Optional.of(eventFullDtoConverter.convertFromEntity(eventFromSave));
    }
}
