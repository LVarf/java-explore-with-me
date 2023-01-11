package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.service.categoryService.CategoryPublicService;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;
import ru.practicum.ewmcore.service.compilationService.CompilationPublicService;
import ru.practicum.ewmcore.service.eventService.EventPublicService;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;
import ru.practicum.ewmcore.statClient.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class PublicController {
    private static final String SORT_EVENT_DATE = "EVENT_DATE";
    private static final String EVENT_DATE_CONST = "eventDate";
    private static final String CREATE_DATE_CONST = "createDate";
    private final CommentInternalService commentService;
    private final EventPublicService eventService;
    private final CompilationPublicService compilationService;
    private final CategoryPublicService categoryService;
    private final TimeUtils timeUtils;
    private final StatClient statClient;

    @GetMapping("/comments/{eventId}")
    public List<CommentDto> readAllComments(@PathVariable Long eventId,
                                            @PageableDefault(sort = {CREATE_DATE_CONST},
                                                    direction = Sort.Direction.ASC) Pageable pageable) {
        return commentService.readAllComments(eventId, pageable);
    }

    @GetMapping("/comments/comment/{comId}")
    public Optional<CommentDto> readComment(@PathVariable Long comId) {
        return commentService.readComment(comId);
    }

    @GetMapping("/events")
    public List<EventShortDto> readAllEvents(@RequestParam(value = "text", required = false) String text,
                                             @RequestParam(value = "categories", required = false) Long[] categories,
                                             @RequestParam(value = "paid", required = false) Boolean paid,
                                             @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                             @RequestParam(value = "sort", required = false) String sort,
                                             Pageable pageable,
                                             HttpServletRequest request) {
        statClient.saveHit(request);
        log.info("Input dates PublicController.readAllEvents: text: {}, categories: {}, paid: {}, " +
                        "rangeStart: {}, rangeEnd: {}, onlyAvailable: {}, sort: {}, pageable: {}, request: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, pageable, request);
        final List<ClientFilterParam> filterParams = new ArrayList<>();
        if (text != null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LIKE)
                    .setProperty("annotation").setMainValue(text));
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LIKE)
                    .setProperty("description").setMainValue(text));
        }
        if (categories != null && categories.length > 0) {
            for (Long id : categories) {
                filterParams.add(new ClientFilterParam().setOperator(Comparison.EQ)
                        .setProperty("category").setMainValue(id));
            }
        }
        if (paid != null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.EQ)
                    .setProperty("paid").setMainValue(paid));
        }
        if (rangeStart == null && rangeEnd == null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.GE)
                    .setProperty("rangeStart").setMainValue(timeUtils.timestampToString(timeUtils.now())));
        }
        if (rangeStart != null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.GE)
                    .setProperty("rangeStart").setMainValue(rangeStart));
        }
        if (rangeEnd != null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LE)
                    .setProperty("rangeStart").setMainValue(rangeEnd));
        }
        if (onlyAvailable) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LT)
                    .setProperty("confirmedRequests").setMainValue("participantLimit"));
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        if (sort != null && sort.equals(SORT_EVENT_DATE)) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(EVENT_DATE_CONST).descending());
        }
        final ClientFilter clientFilter = new ClientFilter(filterParams);
        final var result = eventService.readAllEventsPublic(clientFilter, sort, pageable);
        log.info("Output dates PublicController.readAllEvents: result: {}", result);
        return result;
    }

    @GetMapping("/events/{id}")
    public Optional<EventFullDto> readEvent(@PathVariable Long id, HttpServletRequest request) {
        statClient.saveHit(request);
        log.info("Input dates PublicController.readEvent: text: {}, categories: {}", id, request);
        final var result = eventService.readEventPublic(id);
        log.info("Output dates PublicController.readEvent: result: {}", result);
        return result;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> readAllCompilations(
            @RequestParam(value = "pinned", defaultValue = "true") Boolean pinned,
            Pageable pageable) {
        log.info("Input dates PublicController.readAllCompilations: pinned: {}, pageable: {}", pinned, pageable);
        final var result = compilationService.readAllCompilationsPublic(pinned, pageable);
        log.info("Output dates PublicController.readAllCompilations: result: {}", result);
        return result;
    }

    @GetMapping("/compilations/{compId}")
    public Optional<CompilationDto> readCompilation(@PathVariable Long compId) {
        log.info("Input dates PublicController.readCompilation: compId: {}", compId);
        final var result = compilationService.readCompilationPublic(compId);
        log.info("Output dates PublicController.readCompilation: result: {}", result);
        return result;
    }

    @GetMapping("/categories")
    public List<CategoryDto> readAllCategories(Pageable pageable) {
        log.info("Input dates PublicController.readAllCategories: pageable: {}", pageable);
        final var result = categoryService.readAllCategoriesPublic(pageable);
        log.info("Output dates PublicController.readAllCategories: result: {}", result);
        return result;
    }

    @GetMapping("/categories/{catId}")
    public Optional<CategoryDto> readCategory(@PathVariable Long catId) {
        log.info("Input dates PublicController.readCategory: catId: {}", catId);
        final var result = categoryService.readCategoryPublic(catId);
        log.info("Output dates PublicController.readCategory: result: {}", result);
        return result;
    }
}
