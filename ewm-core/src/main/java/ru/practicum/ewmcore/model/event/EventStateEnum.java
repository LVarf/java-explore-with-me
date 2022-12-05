package ru.practicum.ewmcore.model.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventStateEnum {
    PENDING(1), //в ожидании
    PUBLISHED(2), //опубликован
    CANCELED(3),
    REJECTED(4); //отменён

    private final int id;
}
