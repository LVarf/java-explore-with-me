package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import ru.practicum.ewmcore.converter.CompilationDtoResponseConverter;
import ru.practicum.ewmcore.converter.EventFullDtoResponseConverter;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.compilation.CompilationDtoResponse;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventFullDtoResponse;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.service.adminService.AdminPublicService;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin")
public class AdminController {
    private final AdminPublicService adminService;
    private final EventFullDtoResponseConverter eventResponseConverter;
    private final CompilationDtoResponseConverter compilationResponseConverter;

    @GetMapping("/events")
    public List<EventFullDto> readAllEvents(@RequestParam(value = "users", required = false) Long[] users,
                                            @RequestParam(value = "states", required = false) EventStateEnum[] states,
                                            @RequestParam(value = "categories", required = false) Long[] categories,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            Pageable pageable) {
        log.info("Input dates AdminController.readAllEvents: users: {}, states: {}, categories: {}, rangeStart: {}, " +
                        "rangeEnd: {}, pageable: {}",
                users, states, categories, rangeStart, rangeEnd, pageable);
        final List<ClientFilterParam> params = new ArrayList<>();
        if (users != null && users.length > 0) {
            for (Long user : users) {
                params.add(new ClientFilterParam().setProperty("initiator")
                        .setOperator(Comparison.EQ).setMainValue(user));
            }
        }
        if (states != null && states.length > 0) {
            for (EventStateEnum state : states) {
                params.add(new ClientFilterParam().setProperty("state")
                        .setOperator(Comparison.EQ).setMainValue(state));
            }
        }
        if (categories != null && categories.length > 0) {
            for (Long category : categories) {
                params.add(new ClientFilterParam().setProperty("category")
                        .setOperator(Comparison.EQ).setMainValue(category));
            }
        }
        if (rangeStart != null) {
            params.add(new ClientFilterParam().setProperty("rangeStart").setOperator(Comparison.GE).setMainValue(rangeStart));
        }
        if (rangeEnd != null) {
            params.add(new ClientFilterParam().setProperty("rangeEnd").setOperator(Comparison.LE).setMainValue(rangeEnd));
        }
        final ClientFilter filters = new ClientFilter(params);
        final var result = adminService.readAllByFilters(filters, pageable).toList();
        log.info("Output dates AdminController.readAllEvents: result: {}", result);
        return result;
    }

    @PutMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEvent(@PathVariable Long eventId,
                                              @RequestBody EventFullDtoResponse eventResponse) {
        log.info("Input dates AdminController.updateEvent: eventId: {}, EventFullDtoResponse: {}",
                eventId, eventResponse);
        final var result = adminService.updateEventById(eventId, eventResponseConverter.convertToEntity(eventResponse));
        log.info("Output dates AdminController.updateEvent: result: {}", result);
        return result;
    }

    @PatchMapping("/events/{eventId}/publish")
    public Optional<EventFullDto> publishEvent(@PathVariable Long eventId) {
        log.info("Input dates AdminController.publishEvent: eventId: {}", eventId);
        final var result = adminService.publishEvent(eventId);
        log.info("Output dates AdminController.publishEvent: result: {}", result);
        return result;
    }

    @PatchMapping("/events/{eventId}/reject")
    public Optional<EventFullDto> rejectEvent(@PathVariable Long eventId) {
        log.info("Input dates AdminController.rejectEvent: eventId: {}", eventId);
        final var result = adminService.rejectEvent(eventId);
        log.info("Output dates AdminController.rejectEvent: result: {}", result);
        return result;
    }

    @PatchMapping("/categories")
    public Optional<CategoryDto> updateCategory(@RequestBody CategoryDto category) {
        log.info("Input dates AdminController.updateCategory: CategoryDto: {}", category);
        final var result = adminService.updateCategory(category);
        log.info("Output dates AdminController.updateCategory: result: {}", result);
        return result;
    }

    @PostMapping("/categories")
    public Optional<CategoryDto> createCategory(@RequestBody CategoryDto category) {
        log.info("Input dates AdminController.createCategory: CategoryDto: {}", category);
        final var result = adminService.createCategory(category);
        log.info("Output dates AdminController.createCategory: result: {}", result);
        return result;
    }

    @DeleteMapping("/categories/{catId}")
    public String deleteCategory(@PathVariable Long catId) {
        log.info("Input dates AdminController.deleteCategory: catId: {}", catId);
        final var result = adminService.deleteCategory(catId);
        log.info("Output dates AdminController.deleteCategory: result: {}", result);
        return result;
    }

    @GetMapping("/users")
    public List<UserFullDto> readUser(@RequestParam(value = "ids", required = false) Long[] ids,
                                      Pageable pageable) {
        log.info("Input dates AdminController.readUser: ids: {}, pageable: {}", ids, pageable);
        final var filterParams = new ArrayList<ClientFilterParam>();
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                final var filterParam = new ClientFilterParam();
                filterParam.setOperator(Comparison.EQ);
                filterParam.setProperty("id");
                filterParam.setMainValue(id);
                filterParams.add(filterParam);
            }
        }
        final var filter = new ClientFilter(filterParams);
        final var result = adminService.findAllUsers(filter, pageable).toList();
        log.info("Output dates AdminController.readUser: result: {}", result);
        return result;
    }

    @PostMapping("/users")
    public Optional<UserFullDto> createUser(@RequestBody @Valid UserFullDto user) {
        log.info("Input dates AdminController.createUser: UserFullDto: {}", user);
        final var result = adminService.createUser(user);
        log.info("Output dates AdminController.createUser: result: {}", result);
        return result;
    }

    @DeleteMapping("/users/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        log.info("Input dates AdminController.deleteUser: eventId: {}", userId);
        final var result = adminService.deleteUser(userId);
        log.info("Output dates AdminController.deleteUser: result: {}", result);
        return result;
    }

    @PostMapping("/compilations")
    public Optional<CompilationDto> createCompilation(@RequestBody CompilationDtoResponse compilationResponse) {
        log.info("Input dates AdminController.CompilationDtoResponse: eventId: {}", compilationResponse);
        final var result = adminService
                .createCompilation(compilationResponseConverter.convertToEntity(compilationResponse));
        log.info("Output dates AdminController.createCompilation: result: {}", result);
        return result;
    }

    @DeleteMapping("/compilations/{compId}")
    public String deleteCompilation(@PathVariable Long compId) {
        log.info("Input dates AdminController.deleteCompilation: compId: {}", compId);
        final var result = adminService.deleteCompilation(compId);
        log.info("Output dates AdminController.deleteCompilation: result: {}", result);
        return result;
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public String deleteEventFromCompilation(@PathVariable Long eventId,
                                             @PathVariable Long compId) {
        log.info("Input dates AdminController.deleteEventFromCompilation: eventId: {}, compId: {}", eventId, compId);
        final var result = adminService.deleteEventFromCompilation(eventId, compId);
        log.info("Output dates AdminController.deleteEventFromCompilation: result: {}", result);
        return result;
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public String addEventToCompilation(@PathVariable Long eventId, @PathVariable Long compId) {
        log.info("Input dates AdminController.addEventToCompilation: eventId: {}, compId: {}", eventId, compId);
        final var result = adminService.updateAddEventToCompilation(eventId, compId);
        log.info("Output dates AdminController.addEventToCompilation: result: {}", result);
        return result;
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public String deleteCompilationFromHeadPage(@PathVariable Long compId) {
        log.info("Input dates AdminController.deleteCompilationFromHeadPage: compId: {}", compId);
        final var result = adminService.deleteCompilationFromHeadPage(compId);
        log.info("Output dates AdminController.deleteCompilationFromHeadPage: result: {}", result);
        return result;
    }

    @PatchMapping("/compilations/{compId}/pin")
    public String addCompilationToHeadPage(@PathVariable Long compId) {
        log.info("Input dates AdminController.addCompilationToHeadPage: compId: {}", compId);
        final var result = adminService.addCompilationToHeadPage(compId);
        log.info("Output dates AdminController.addCompilationToHeadPage: result: {}", result);
        return result;
    }

    @GetMapping("/comments")
    public Page<CommentDto> readAllComments(@RequestParam(value = "eventId", required = false) Long eventId,
                                            @RequestParam(value = "commentOwner", required = false) Long commentOwner,
                                            @PageableDefault(sort = {"createDate"}, direction = Sort.Direction.DESC)
                                            Pageable pageable) {
        //сортировка по дате создания
        return null;
    }

    @GetMapping("/comments/{comId}")
    public Optional<CommentDto> readComment(@PathVariable Long comId) {
        return null;
    }

    @DeleteMapping("/comments/{comId}")
    public String updateEventOnCancel(@PathVariable Long comId) {
        return null;
    }

}
