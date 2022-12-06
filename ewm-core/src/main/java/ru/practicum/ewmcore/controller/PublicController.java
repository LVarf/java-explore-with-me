package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.error.ErrorHandler;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventShortDto;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class PublicController {
    private final ErrorHandler errorHandler;

    /*@GetMapping("/some/path/{id}") //про ip клиента
public void logIPAndPath(@PathVariable long id, HttpServletRequest request) {
    log.info("client ip: {}", request.getRemoteAddr());
    log.info("endpoint path: {}", request.getRequestURI());
} */

    @GetMapping("/events")
    public Page<EventShortDto> readAllEvents(@RequestParam("text") String text,
                                             @RequestParam(value = "categories", required = false) Long categories,
                                             @RequestParam(value = "paid", required = false) Boolean paid,
                                             @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                             Pageable pageable) {

        return Page.empty();
    }

    @GetMapping("/events/{id}")
    public Optional<EventShortDto> readEvent(@PathVariable Long id) {
        return Optional.empty();
    }

    @GetMapping("/compilations")
    public Page<CompilationDto> readAllCompilations(@RequestParam("pinned") Boolean pinned,
                                                    @RequestParam("from") Integer from,
                                                    @RequestParam("size") Integer size,
                                                    Pageable pageable) {
        return Page.empty();
    }

    @GetMapping("/compilations/{compId}")
    public Optional<CompilationDto> readCompilation(@PathVariable Long compId) {
        return Optional.empty();
    }

    @GetMapping("/categories")
    public Page<CategoryDto> readAllCategories(@RequestParam("from") Integer from,
                                               @RequestParam("size") Integer size,
                                               Pageable pageable) {
        return Page.empty();
    }

    @GetMapping("/compilations/{catId}")
    public Optional<CategoryDto> readCategory(@PathVariable Long catId) {
        return Optional.empty();
    }
}
