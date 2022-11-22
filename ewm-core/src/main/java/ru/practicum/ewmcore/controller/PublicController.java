package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventShortDto;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PublicController {

    @GetMapping("/events")
    public List<EventShortDto> readAllEvents(@RequestParam("text") String text,
                                             @RequestParam("categories") Long categories,
                                             @RequestParam("paid") Boolean paid,
                                             @RequestParam("rangeStart") String rangeStart,
                                             @RequestParam("rangeEnd") String rangeEnd,
                                             @RequestParam("sort") String sort,
                                             @RequestParam("from") Integer from,
                                             @RequestParam("size") Integer size,
                                             Pageable pageable) {
        return List.of();
    }

    @GetMapping("/events/{id}")
    public Optional<EventShortDto> readEvent(@PathVariable Long id) {
        return Optional.empty();
    }

    @GetMapping("/compilations")
    public List<CompilationDto> readAllCompilations(@RequestParam("pinned") Boolean pinned,
                                                    @RequestParam("from") Integer from,
                                                    @RequestParam("size") Integer size,
                                                    Pageable pageable) {
        return List.of();
    }

    @GetMapping("/compilations/{compId}")
    public Optional<CompilationDto> readCompilation(@PathVariable Long compId) {
        return Optional.empty();
    }

    @GetMapping("/categories")
    public List<CategoryDto> readAllCategories(@RequestParam("from") Integer from,
                                               @RequestParam("size") Integer size,
                                               Pageable pageable) {
        return List.of();
    }

    @GetMapping("/compilations/{catId}")
    public Optional<CategoryDto> readCategory(@PathVariable Long catId) {
        return Optional.empty();
    }
}
