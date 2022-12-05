package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin")
public class AdminController {
    @GetMapping("/events")
    public Page<EventFullDto> readAllEvents(@RequestParam(value = "users", required = false) Long[] users,
                                            @RequestParam(value = "states", required = false) EventStateEnum[] states,
                                            @RequestParam(value = "categories", required = false) Long[] categories,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            Pageable pageable) {
        final List<ClientFilterParam> params = new ArrayList<>();
        params.add(new ClientFilterParam().setProperty("users").setOperator(Comparison.EQ).setMainValue(users));
        params.add(new ClientFilterParam().setProperty("states").setOperator(Comparison.EQ).setMainValue(states));
        params.add(new ClientFilterParam().setProperty("categories")
                .setOperator(Comparison.EQ).setMainValue(categories));
        params.add(new ClientFilterParam().setProperty("rangeStart").setOperator(Comparison.GE).setMainValue(users));
        params.add(new ClientFilterParam().setProperty("rangeEnd").setOperator(Comparison.LE).setMainValue(users));
        final ClientFilter filters = new ClientFilter(params);
        return Page.empty();
    }

    @PutMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEvent(@PathVariable Long eventId,
                                              @RequestBody EventFullDto event) {
        return Optional.empty();
    }

    @PatchMapping("/events/{eventId}/publish")
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId) {
        return Optional.empty();
    }

    @PatchMapping("/events/{eventId}/reject")
    public Page<EventFullDto> readEvent(@PathVariable Long eventId) {
        return Page.empty();
    }

    @PatchMapping("/categories")
    public Optional<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        return Optional.of(category);
    }

    @PostMapping("/categories")
    public Optional<CategoryDto> createCategory(@RequestBody CategoryDto category) {
        return Optional.of(category);
    }

    @DeleteMapping("/categories/{catId}")
    public String deleteCategory(@PathVariable Long catId) {
        return null;
    }

    @GetMapping("/users")
    public Page<UserFullDto> readUser(@RequestParam(value = "ids", required = false) Long[] ids,
                                            /*@RequestParam(value = "from", defaultValue = "0") int from,
                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                             @RequestParam("size") Integer size,*/
                                      Pageable pageable) {
        return Page.empty();
    }

    @PostMapping("/users")
    public Optional<UserFullDto> createUser(@RequestBody UserFullDto user) {
        return Optional.of(user);
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        return null;
    }

    @PostMapping("/compilations")
    public Optional<CompilationDto> createCompilation(@RequestBody CompilationDto compilation) {
        return Optional.of(compilation);
    }

    @DeleteMapping("/compilations/{compId}")
    public String deleteCompilation(@PathVariable Long compId) {
        return null;
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public String deleteEventFromCompilation(@PathVariable Long compId,
                                             @PathVariable Long eventId) {
        return null;
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public String addEventToCompilation(@PathVariable Long compId,
                                        @PathVariable Long eventId) {
        return null;
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public String deleteCompilationFromHeadPage(@PathVariable Long compId) {
        return null;
    }

    @PatchMapping("/compilations/{compId}/pin")
    public String addCompilationToHeadPage(@PathVariable Long compId) {
        return null;
    }

}
