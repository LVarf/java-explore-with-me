package ru.practicum.ewmcore.service.compilationService;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.compilation.CompilationDto;

import java.util.List;
import java.util.Optional;

public interface CompilationPublicService {
    List<CompilationDto> readAllCompilationsPublic(Boolean pinned, Pageable pageable);

    Optional<CompilationDto> readCompilationPublic(Long compId);
}
