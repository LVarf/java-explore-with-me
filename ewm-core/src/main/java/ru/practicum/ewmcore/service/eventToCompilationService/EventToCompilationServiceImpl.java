package ru.practicum.ewmcore.service.eventToCompilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.repository.EventToCompilationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventToCompilationServiceImpl implements EventToCompilationInternalService {
    private final EventToCompilationRepository repository;

    @Override
    public EventToCompilation createEventToCompilation(Event event, Compilation compilation) {
        final var eventToCompilation = new EventToCompilation();
        eventToCompilation.setEvent(event);
        eventToCompilation.setCompilation(compilation);
        return repository.save(eventToCompilation);
    }
}
