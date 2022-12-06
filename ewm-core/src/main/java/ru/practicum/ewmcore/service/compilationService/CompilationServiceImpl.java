package ru.practicum.ewmcore.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CompilationDtoConverter;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.repository.CompilationRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.eventToCompilationService.EventToCompilationInternalService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationInternalService {
    private static final String COMPILATION_IS_DELETED = "Подборка удалена";
    private static final String COMPILATION_IS_NOT_DELETED = "Подборка не удалена";
    private static final String EVENT_IS_ADDED_TO_COMPILATION = "Событие добавлено";
    private static final String EVENT_IS_NOT_ADDED_TO_COMPILATION= "Событие не добавлено";
    private final CompilationRepository repository;
    private final EventInternalService eventService;
    private final CompilationDtoConverter converter;
    private final EventToCompilationInternalService eventToCompilationService;
    private final EventFullDtoConverter eventFullDtoConverter;

    @Override
    public Optional<CompilationDto> createCompilationInternal(CompilationDto compilationDto) {
        final var compilationFromSave = repository.save(converter.convertToEntity(compilationDto));
        return Optional.of(enrichEventToCompilations(compilationDto.getEvents(), compilationFromSave))
                .map(converter::convertFromEntity);
    }

    @Override
    public String deleteCompilationInternal(Long compId) {
        final var compilationFromDb = repository.findById(compId).orElse(null);
        if (compilationFromDb != null) {
            repository.delete(compilationFromDb);
            return COMPILATION_IS_DELETED;
        }
        return COMPILATION_IS_NOT_DELETED;
    }

    @Override
    public String deleteEventFromCompilationInternal(Long eventId, Long compId) {
        return eventToCompilationService.deleteEventFromCompilation(eventId, compId);
    }

    @Override
    public String updateAddEventToCompilationInternal(Long eventId, Long compId) {
        final var compilationFromDb = repository.findById(compId).orElse(null);
        final var eventFromDb = eventService.readEvent(eventId).orElse(null);
        if (compilationFromDb != null && eventFromDb != null) {
            eventToCompilationService
                    .createEventToCompilation(eventFullDtoConverter.convertToEntity(eventFromDb), compilationFromDb);
            return EVENT_IS_ADDED_TO_COMPILATION;
        }
        return EVENT_IS_NOT_ADDED_TO_COMPILATION;
    }

    private Compilation enrichEventToCompilations(Set<Long> eventsIds, Compilation compilation) {
        final Set<EventToCompilation> eventToCompilations = new HashSet<>();
        final var events = eventService.readAllByFilter(eventsIds);
        for (Event event : events) {
            final var eventToCompilation = eventToCompilationService
                    .createEventToCompilation(event, compilation);
            eventToCompilations.add(eventToCompilation);
        }
        compilation.setEventToCompilations(eventToCompilations);
        return compilation;
    }
}
