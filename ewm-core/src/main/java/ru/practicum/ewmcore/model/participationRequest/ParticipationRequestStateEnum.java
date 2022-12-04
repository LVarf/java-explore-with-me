package ru.practicum.ewmcore.model.participationRequest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ParticipationRequestStateEnum {
    PENDING(1),
    CONFIRMED(2),
    REJECTED(3);

    private final int id;
}
