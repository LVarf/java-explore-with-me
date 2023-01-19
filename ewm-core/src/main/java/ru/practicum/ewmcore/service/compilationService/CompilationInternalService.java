package ru.practicum.ewmcore.service.compilationService;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.compilation.CompilationDto;

import java.util.Optional;

@Transactional
public interface CompilationInternalService {

    Optional<CompilationDto> createCompilationInternal(CompilationDto compilationDto);

    String deleteCompilationInternal(Long compId);

    String deleteEventFromCompilationInternal(Long eventId, Long compId);

    String updateAddEventToCompilationInternal(Long eventId, Long compId);

    String deleteCompilationFromHeadPageInternal(Long compId);

    String addCompilationToHeadPageInternal(Long compId);
}
