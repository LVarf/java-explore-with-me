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
import ru.practicum.ewmcore.service.adminService.AdminPublicService;
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
    private final AdminPublicService adminService;

    @GetMapping("/events")
    public Page<EventFullDto> readAllEvents(@RequestParam(value = "users", required = false) Long[] users,
                                            @RequestParam(value = "states", required = false) EventStateEnum[] states,
                                            @RequestParam(value = "categories", required = false) Long[] categories,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            Pageable pageable) {
        final List<ClientFilterParam> params = new ArrayList<>();
        if (users.length > 0) {
            for (int i = 0; i < users.length; i++) {
                params.add(new ClientFilterParam().setProperty("userId")
                        .setOperator(Comparison.EQ).setMainValue(users[i]));
            }
        }
        if (states.length > 0) {
            for (int i = 0; i < states.length; i++) {
                params.add(new ClientFilterParam().setProperty("state")
                        .setOperator(Comparison.EQ).setMainValue(states[i]));
            }
        }
        if (categories.length > 0) {
            for (int i = 0; i < categories.length; i++) {
                params.add(new ClientFilterParam().setProperty("category")
                        .setOperator(Comparison.EQ).setMainValue(categories[i]));
            }
        }
        if (rangeStart != null) {
            params.add(new ClientFilterParam().setProperty("rangeStart").setOperator(Comparison.GE).setMainValue(rangeStart));
        }
        if (rangeEnd != null) {
            params.add(new ClientFilterParam().setProperty("rangeEnd").setOperator(Comparison.LE).setMainValue(rangeEnd));
        }
        final ClientFilter filters = new ClientFilter(params);
        return adminService.readAllByFilters(filters, pageable);
    }

    @PutMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEvent(@PathVariable Long eventId,
                                              @RequestBody EventFullDto event) {
        return adminService.updateEventById(eventId, event);
    }

    @PatchMapping("/events/{eventId}/publish")
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId) {
        return adminService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public Optional<EventFullDto> rejectEvent(@PathVariable Long eventId) {
        return adminService.rejectEvent(eventId);
    }

    @PatchMapping("/categories")
    public Optional<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        return adminService.updateCategory(category);
    }

    @PostMapping("/categories")
    public Optional<CategoryDto> createCategory(@RequestBody CategoryDto category) {
        return adminService.createCategory(category);
    }

    @DeleteMapping("/categories/{catId}")
    public String deleteCategory(@PathVariable Long catId) {
        return adminService.deleteCategory(catId);
    }

    @GetMapping("/users")
    public Page<UserFullDto> readUser(@RequestParam(value = "ids", required = false) Long[] ids,
                                      Pageable pageable) {
        final var filterParams = new ArrayList<ClientFilterParam>();
        if (ids.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                final var filterParam = new ClientFilterParam();
                filterParam.setOperator(Comparison.EQ);
                filterParam.setProperty("userId");
                filterParam.setMainValue(ids[i]);
                filterParams.add(filterParam);
            }
        }
        final var filter = new ClientFilter(filterParams);
        return adminService.findAllUsers(filter, pageable);
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
