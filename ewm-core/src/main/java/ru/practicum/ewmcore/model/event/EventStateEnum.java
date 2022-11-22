package ru.practicum.ewmcore.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventStateEnum {
    PENDING(1),
    PUBLISHED(2),
    CANCELED(3);

    private final int id;
}
