package ru.practicum.ewmcore.model.stat;

import lombok.Data;

@Data
public class EndpointHit {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
