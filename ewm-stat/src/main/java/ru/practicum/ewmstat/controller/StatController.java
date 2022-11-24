package ru.practicum.ewmstat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatController {

    @GetMapping("/events")
    public String/*List<EventShortDto> */readAllEvents(@RequestParam("text") String text/*,
                                             @RequestParam("categories") Long categories,
                                             @RequestParam("paid") Boolean paid,
                                             @RequestParam("rangeStart") String rangeStart,
                                             @RequestParam("rangeEnd") String rangeEnd,
                                             @RequestParam("sort") String sort,
                                             @RequestParam("from") Integer from,
                                             @RequestParam("size") Integer size,
                                             Pageable pageable*/) {
        return /*List.of()*/text;
    }
}
