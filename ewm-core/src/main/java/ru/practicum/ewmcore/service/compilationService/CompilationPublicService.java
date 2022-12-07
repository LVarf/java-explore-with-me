package ru.practicum.ewmcore.service.compilationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.compilation.CompilationDto;

import java.util.Optional;

public interface CompilationPublicService {
    Page<CompilationDto> readAllCompilationsPublic(Boolean pinned, Pageable pageable);

    Optional<CompilationDto> readCompilationPublic(Long compId);
}
