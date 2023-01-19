package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

@Transactional
public interface UserPublicService {
    @Transactional(readOnly = true)
    Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEventPublic(Long userId, Long eventId);

    Optional<EventFullDto> updateEventPublic(Long id, EventFullDto event);

    Optional<EventFullDto> createEventPublic(Long userId, EventFullDto event);

    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    @Transactional(readOnly = true)
    List<ParticipationRequestDto> readRequestPublic(Long userId, Long eventId);

    Optional<ParticipationRequestDto> confirmRequestPublic(Long userId, Long eventId, Long reqId);

    Optional<ParticipationRequestDto> rejectRequestPublic(Long userId, Long eventId, Long reqId);

    @Transactional(readOnly = true)
    Optional<List<ParticipationRequestDto>> readRequests(Long userId);

    Optional<ParticipationRequestDto> createRequestPublic(Long userId, Long eventId);

    Optional<ParticipationRequestDto> updateRequestCanselPublic(Long userId, Long requestsId);
}
