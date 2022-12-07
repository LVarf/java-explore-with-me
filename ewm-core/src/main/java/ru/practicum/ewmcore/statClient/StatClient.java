package ru.practicum.ewmcore.statClient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.stat.EndpointHit;
import ru.practicum.ewmcore.model.stat.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatClient {
    private static final String API_CONST = "ewm-core-service";
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
            return restTemplate.postForObject(new URI(url + "/hit"), hit, String.class);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Object> getViews(String start, String end, String[] uris, Boolean unique) {
        final Map<String, Object> uriVar = new HashMap<>();
        uriVar.put("start", start);
        uriVar.put("end", end);
        uriVar.put("uris", Arrays.toString(uris));
        uriVar.put("unique", unique);
        return restTemplate.getForEntity(url + "/stats", Object.class, uriVar);
    }

//    public ResponseEntity<Object> getViews1( @Nullable Map<String, Object> parameters) {
//        final String path = url + "/stats";
//        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
//    }
//
//    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path,
//                                                          @Nullable Map<String, Object> parameters, @Nullable T body) {
//        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());
//
//        ResponseEntity<Object> statServerResponse;
//        try {
//            if (parameters != null) {
//                statServerResponse = restTemplate.exchange(path, method, requestEntity, Object.class, parameters);
//            } else {
//                statServerResponse = restTemplate.exchange(path, method, requestEntity, Object.class);
//            }
//        } catch (HttpStatusCodeException e) {
//            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
//        }
//        return prepareGatewayResponse(statServerResponse);
//    }
//
//    private HttpHeaders defaultHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        return headers;
//    }
//
//    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
//        if (response.getStatusCode().is2xxSuccessful()) {
//            return response;
//        }
//
//        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());
//
//        if (response.hasBody()) {
//            return responseBuilder.body(response.getBody());
//        }
//
//        return responseBuilder.build();
//    }
}
