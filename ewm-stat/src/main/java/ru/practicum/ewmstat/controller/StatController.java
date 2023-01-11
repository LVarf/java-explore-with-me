package ru.practicum.ewmstat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmstat.model.EndpointHitDto;
import ru.practicum.ewmstat.model.ViewStats;
import ru.practicum.ewmstat.service.StatService;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class StatController {

    private final StatService service;

    @PostMapping("/hit")
    public String createHit(@RequestBody EndpointHitDto hit) {
        return service.createHit(hit);
    }

    @GetMapping("/stats")
    public ViewStats[] readStats(@RequestParam("start") String start,
                                 @RequestParam("end") String end,
                                 @RequestParam(value = "unique", defaultValue = "false") Boolean unique,
                                 @RequestParam("uris") String uris,
                                 HttpServletRequest request) {
        log.info("RequestURI = {}", request.getContextPath());
        log.info("Inter dates: stats={}; end={}; uris={}; unique={}", start, end, uris, unique);
        start = URLDecoder.decode(start, StandardCharsets.UTF_8);
        end = URLDecoder.decode(end, StandardCharsets.UTF_8);
        log.info("Inter dates: stats={}; end={}; uris={}; unique={}", start, end, uris, unique);
        final var result = service.readStats(start, end, uris, unique).toArray(new ViewStats[0]);
        log.info("result: {}", result);
        return result;
    }
}
