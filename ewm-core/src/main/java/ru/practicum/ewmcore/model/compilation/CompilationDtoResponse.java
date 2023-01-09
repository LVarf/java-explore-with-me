package ru.practicum.ewmcore.model.compilation;

import lombok.Data;

import java.util.Set;

@Data
public class CompilationDtoResponse {
    private Long id;
    private Set<Long> events;
    private Boolean pinned;
    private String title;
}
