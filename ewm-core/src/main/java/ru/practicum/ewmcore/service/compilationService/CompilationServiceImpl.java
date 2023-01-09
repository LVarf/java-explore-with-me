package ru.practicum.ewmcore.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CompilationDtoConverter;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.repository.CompilationRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.eventToCompilationService.EventToCompilationInternalService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationInternalService, CompilationPublicService {
    private static final String COMPILATION_IS_DELETED = "Подборка удалена";
    private static final String COMPILATION_IS_NOT_DELETED = "Подборка не удалена";
    private static final String EVENT_IS_ADDED_TO_COMPILATION = "Событие добавлено";
    private static final String EVENT_IS_NOT_ADDED_TO_COMPILATION = "Событие не добавлено";
    private static final String COMPILATION_PINNED_IS_TRUE_SUCCESS = "Подборка закреплена";
    private static final String COMPILATION_PINNED_IS_TRUE_FAIL = "Подборка не закреплена";
    private static final String COMPILATION_PINNED_IS_FALSE_SUCCESS = "Подборка откреплена";
    private static final String COMPILATION_PINNED_IS_FALSE_FAIL = "Подборка не откреплена";
    private final CompilationRepository repository;
    private final EventInternalService eventService;
    private final CompilationDtoConverter converter;
    private final EventToCompilationInternalService eventToCompilationService;
    private final EventFullDtoConverter eventFullDtoConverter;

    private final EventShortDtoConverter eventShortDtoConverter;

    @Override
    public List<CompilationDto> readAllCompilationsPublic(Boolean pinned, Pageable pageable) {
        return repository.findCompilationByPinnedIs(pinned, pageable).map(converter::convertFromEntity)
                .map(this::enrichEventToCompilationImpl).toList();
    }

    @Override
    public Optional<CompilationDto> readCompilationPublic(Long compId) {
        return repository.findById(compId).map(converter::convertFromEntity).map(this::enrichEventToCompilationImpl);
    }

    private CompilationDto enrichEventToCompilationImpl(CompilationDto compilation) {
        final var eventToComp = eventToCompilationService.readEventToCompilation(compilation.getId())
                .stream().map(EventToCompilation::getEvent)
                .map(eventShortDtoConverter::convertFromEntity).map(EventShortDto::getId).collect(Collectors.toSet());
        compilation.setEvents(eventToComp);
        return compilation;
    }

    @Override
    public Optional<CompilationDto> createCompilationInternal(CompilationDto compilationDto) {
        final var compilationFromSave = repository.save(converter.convertToEntity(compilationDto));
        saveEventToCompilations(compilationDto.getEvents(), compilationFromSave);
        final var eventsList = eventToCompilationService.readEventToCompilation(compilationFromSave.getId())
                .stream().map(el -> el.getEvent().getId()).collect(Collectors.toSet());
        final var compilationForReturn = converter.convertFromEntity(compilationFromSave);
        compilationForReturn.setEvents(eventsList);
        return Optional.of(compilationForReturn);
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

    @Override
    public String deleteCompilationFromHeadPageInternal(Long compId) {
        final var compilationFromDb = repository.findById(compId).orElse(null);
        if (compilationFromDb != null) {
            compilationFromDb.setPinned(false);
            repository.save(compilationFromDb);
            return COMPILATION_PINNED_IS_FALSE_SUCCESS;
        }
        return COMPILATION_PINNED_IS_FALSE_FAIL;
    }

    @Override
    public String addCompilationToHeadPageInternal(Long compId) {
        final var compilationFromDb = repository.findById(compId).orElse(null);
        if (compilationFromDb != null) {
            compilationFromDb.setPinned(true);
            repository.save(compilationFromDb);
            return COMPILATION_PINNED_IS_TRUE_SUCCESS;
        }
        return COMPILATION_PINNED_IS_TRUE_FAIL;
    }

    private Compilation saveEventToCompilations(Set<Long> eventsSet, Compilation compilation) {
        final var events = eventService.readAllByIds(eventsSet);
        if (events.size() > 0) {
            for (Event event : events) {
                eventToCompilationService.createEventToCompilation(event, compilation);
            }
        }
        return compilation;
    }
}
