package ru.practicum.ewmcore.service.participationRequestService;

import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestInternalService {

    Optional<ParticipationRequestDto> findRequestRequesterOnEvent(Long userId, Long eventId);

    List<ParticipationRequestDto> findRequestsByUserAndEvent(Long eventId);

    Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId);

    Optional<ParticipationRequestDto> rejectRequest(Long userId, Long eventId, Long reqId);

    Optional<List<ParticipationRequestDto>> readRequestsByRequesterId(Long requesterId);

    Optional<ParticipationRequestDto> createRequest(Long userId, Long eventId);

    Optional<ParticipationRequestDto> updateRequestCansel(Long userId, Long eventId);
}
