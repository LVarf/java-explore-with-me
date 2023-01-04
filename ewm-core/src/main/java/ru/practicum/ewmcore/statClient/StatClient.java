package ru.practicum.ewmcore.statClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.stat.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatClient {
    private static final String API_CONST = "ewm-core-service";
    private static final String HIT_ENDPOINT = "/hit";
    private static final String STATS_ENDPOINT = "/stats";
    @Value("${stats-server.url}")
    private String url;
    private final TimeUtils timeUtils;
    private final RestTemplate restTemplate = new RestTemplate();


    public String saveHit(HttpServletRequest request) {
        final var hit = new EndpointHit();
        hit.setApp(API_CONST);
        hit.setIp(request.getRemoteAddr());
        hit.setUri(request.getRequestURI());
        hit.setTimestamp(timeUtils.timestampToString(timeUtils.now()));
        try {
            return restTemplate.postForObject(new URI(url + HIT_ENDPOINT), hit, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Object> getViews(String start, String end, String[] uris, Boolean unique) {
        final Map<String, Object> uriVar = new HashMap<>();

        final var request = buildRequestHeaders();
        uriVar.put("start", start);
        uriVar.put("end", end);
        uriVar.put("uris", Arrays.toString(uris));
        uriVar.put("unique", unique);
        final var newUrl = UriComponentsBuilder.fromHttpUrl(url + STATS_ENDPOINT)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", Arrays.toString(uris))
                .queryParam("unique", unique)
                .build().toUri();
        log.info("newUri = {}", newUrl);
        final String path = url + STATS_ENDPOINT + "?start=" + start + "&end=" + end + "&uris="
                + Arrays.toString(uris) + "$unique=" + unique;
        log.info(Arrays.toString(uris));
        log.info(path);
        try {
            final ResponseEntity<Object> response = restTemplate.getForEntity(path, Object.class, request);
            log.info(response.getBody().toString());
            return response;
        } catch (RuntimeException e) {
            throw new ApiError().setStatus(HttpStatus.BAD_REQUEST).setReason("Ошибка запроса в сервис статистики")
                    .setTimestamp(timeUtils.timestampToString(timeUtils.now()))
                    .setMessage("path = " + path);
        }
    }

    private HttpEntity<Void> buildRequestHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>(headers);
    }
}
