package ru.practicum.ewmcore.model.compilation;

import lombok.Data;
import ru.practicum.ewmcore.model.event.EventShortDto;

import java.util.Set;

@Data
public class CompilationDto {
    private Long id;
    private Set<Long> events;
    private Boolean pinned;
    private String title;
}
