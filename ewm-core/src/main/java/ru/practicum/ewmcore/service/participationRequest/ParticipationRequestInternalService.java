package ru.practicum.ewmcore.service.participationRequest;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.Optional;

public interface ParticipationRequestInternalService {

    Optional<ParticipationRequestDto> findRequestUserOnEvent(Long userId, Long eventId);

    Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId);
}
