package ru.practicum.ewmcore.service.eventToCompilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.model.records.EventToCompilationKey;
import ru.practicum.ewmcore.repository.EventToCompilationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventToCompilationServiceImpl implements EventToCompilationInternalService {

    private static final String EVENT_FROM_COMPILATION_IS_DELETED = "Событие удалено из подборки";
    private static final String EVENT_FROM_COMPILATION_IS_NOT_DELETED = "Событие не удалено из подборки";
    private final EventToCompilationRepository repository;

    @Override
    public EventToCompilation createEventToCompilation(Event event, Compilation compilation) {
        final var eventToCompilation = new EventToCompilation();
        eventToCompilation.setKey(new EventToCompilationKey(event.getId(), compilation.getId()));
        eventToCompilation.setEvent(event);
        eventToCompilation.setCompilation(compilation);
        return repository.save(eventToCompilation);
    }

    @Override
    public String deleteEventFromCompilation(Long eventId, Long compId) {
        final var eventToCompilationFromDb = repository
                .findByEventIdAndCompilationId(eventId, compId).orElse(null);
        if (eventToCompilationFromDb != null) {
            repository.delete(eventToCompilationFromDb);
            return EVENT_FROM_COMPILATION_IS_DELETED;
        }
        return EVENT_FROM_COMPILATION_IS_NOT_DELETED;
    }
}
