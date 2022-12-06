package ru.practicum.ewmcore.service.compilationService;

import ru.practicum.ewmcore.model.compilation.CompilationDto;

import java.util.Optional;

public interface CompilationInternalService {

    Optional<CompilationDto> createCompilationInternal(CompilationDto compilationDto);
}
