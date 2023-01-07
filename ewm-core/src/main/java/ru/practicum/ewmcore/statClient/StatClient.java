package ru.practicum.ewmcore.statClient;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.stat.EndpointHit;
import ru.practicum.ewmcore.model.stat.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public List<ViewStats> getViews(String start, String end, String uris, Boolean unique) {
        final var request = buildRequestHeaders();
        final String path;
        try {
            path = url + STATS_ENDPOINT + "?unique=" + unique + "&uris=" + uris +
                    "&start=" + URLEncoder.encode(start, "UTF-8") +
                    "&end=" + URLEncoder.encode(end, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        try {
            final ResponseEntity<Object[]> response = restTemplate.getForEntity(path, Object[].class, request);
            final var mapper = new ObjectMapper();
            return Arrays.stream(Objects.requireNonNull(response.getBody()))
                    .map(el -> mapper.convertValue(el, ViewStats.class)).collect(Collectors.toList());
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
