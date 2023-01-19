package ru.practicum.ewmcore.service.participationRequestService;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ParticipationRequestInternalService {

    @Transactional(readOnly = true)
    Optional<ParticipationRequestDto> findRequestRequesterOnEvent(Long userId, Long eventId);

    @Transactional(readOnly = true)
    List<ParticipationRequestDto> findRequestsByUserAndEvent(Long eventId);

    Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId);

    Optional<ParticipationRequestDto> rejectRequest(Long userId, Long eventId, Long reqId);

    @Transactional(readOnly = true)
    Optional<List<ParticipationRequestDto>> readRequestsByRequesterId(Long requesterId);

    Optional<ParticipationRequestDto> createRequest(Long userId, Long eventId);

    Optional<ParticipationRequestDto> updateRequestCansel(Long userId, Long eventId);
}
