package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.service.categoryService.CategoryPublicService;
import ru.practicum.ewmcore.service.compilationService.CompilationPublicService;
import ru.practicum.ewmcore.service.eventService.EventPublicService;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class PublicController {

    private final EventPublicService eventService;
    private final CompilationPublicService compilationService;
    private final CategoryPublicService categoryService;
    private final TimeUtils timeUtils;

    /*@GetMapping("/some/path/{id}") //про ip клиента
public void logIPAndPath(@PathVariable long id, HttpServletRequest request) {
    log.info("client ip: {}", request.getRemoteAddr());
    log.info("endpoint path: {}", request.getRequestURI());
} */

    @GetMapping("/events")
    public Page<EventShortDto> readAllEvents(@RequestParam(value = "text", required = false) String text,
                                             @RequestParam(value = "categories", required = false) Long[] categories,
                                             @RequestParam(value = "paid", required = false) Boolean paid,
                                             @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                             @RequestParam(value = "onlyAvailable", defaultValue = "false")
                                             Boolean onlyAvailable,
                                             @RequestParam(value = "sort", required = false) String sort,
                                             Pageable pageable,
                                             HttpServletRequest request) {
        //сортировка либо по датам событий, либо по количеству просмотров
        //созранить статистику
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        //TODO: отправить запрос в статитстику
        final List<ClientFilterParam> filterParams = new ArrayList<>();
        if (text != null) {
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LIKE_IGNORE_CASE)
                    .setProperty("annotation").setMainValue(text));
            filterParams.add(new ClientFilterParam().setOperator(Comparison.LIKE_IGNORE_CASE)
                    .setProperty("description").setMainValue(text));
        }
        if (categories.length > 0) {
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
                    .setProperty("rangeStart").setMainValue(timeUtils.now()));
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
        final ClientFilter clientFilter = new ClientFilter(filterParams);
        return eventService.readAllEventsPublic(clientFilter, sort, pageable);
    }

    @GetMapping("/events/{id}")
    public Optional<EventFullDto> readEvent(@PathVariable Long id) {
        //созранить статистику
        return eventService.readEventPublic(id);
    }

    @GetMapping("/compilations")
    public Page<CompilationDto> readAllCompilations(@RequestParam("pinned") Boolean pinned,
                                                    Pageable pageable) {
        return compilationService.readAllCompilationsPublic(pinned, pageable);
    }

    @GetMapping("/compilations/{compId}")
    public Optional<CompilationDto> readCompilation(@PathVariable Long compId) {
        return compilationService.readCompilationPublic(compId);
    }

    @GetMapping("/categories")
    public Page<CategoryDto> readAllCategories(Pageable pageable) {
        return categoryService.readAllCategoriesPublic(pageable);
    }

    @GetMapping("/compilations/{catId}")
    public Optional<CategoryDto> readCategory(@PathVariable Long catId) {
        return categoryService.readCategoryPublic(catId);
    }
}
