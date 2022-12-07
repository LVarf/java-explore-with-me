package ru.practicum.ewmstat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmstat.model.EndpointHitDto;
import ru.practicum.ewmstat.model.ViewStats;
import ru.practicum.ewmstat.service.StatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    public String createHit(@RequestBody EndpointHitDto hit) {
        return service.createHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> readStats(@RequestParam("start") String start,
                                     @RequestParam("end") String end,
                                     @RequestParam("uris") String[] uris,
                                     @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {
        return service.readStats(start, end, uris, unique);
    }
}
