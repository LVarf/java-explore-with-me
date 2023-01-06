package ru.practicum.ewmstat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmstat.model.EndpointHitDto;
import ru.practicum.ewmstat.model.ViewStats;
import ru.practicum.ewmstat.repository.StatRepository;
import ru.practicum.ewmstat.utils.EndpointHitDtoConverter;
import ru.practicum.ewmstat.utils.TimeUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatService {

    private static final String ADD_SUCCESS = "Информация добалена";
    private static final String ADD_FAIL = "Информация не добавлена";
    private final EndpointHitDtoConverter converter;
    private final StatRepository repository;
    private final TimeUtils timeUtils;

    public String createHit(EndpointHitDto hit) {
        final var hitFromDb = repository.save(converter.convertToEntity(hit));
        if (hitFromDb != null) {
            return ADD_SUCCESS;
        }
        return ADD_FAIL;
    }

    public List<ViewStats> readStats(String start, String end, String uris, Boolean unique) {
        final List<ViewStats> result;
        if (unique) {
            result = repository
                    .findViewsUniqueTrue(timeUtils.stringToTimestamp(start),
                            timeUtils.stringToTimestamp(end), uris);
            log.info("Unique = true: " + result.toString());
        } else {
            result = repository
                    .findViewsUniqueFalse(timeUtils.stringToTimestamp(start),
                            timeUtils.stringToTimestamp(end), uris);
            log.info("Unique = false: " + result.toString());
        }
        return result;
    }
}
