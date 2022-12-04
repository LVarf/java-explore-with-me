package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserPublicService {
    @Transactional(readOnly = true)
    Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<EventFullDto> readEventPublic(Long userId, Long eventId);

    @Transactional
    Optional<EventFullDto> updateEventPublic(Long id, EventFullDto event);

    @Transactional
    Optional<EventFullDto> createEventPublic(Long userId, EventFullDto event);

    @Transactional
    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    @Transactional(readOnly = true)
    Optional<ParticipationRequestDto> readRequestPublic(Long userId, Long eventId);

    @Transactional
    Optional<ParticipationRequestDto> confirmRequestPublic(Long userId, Long eventId, Long reqId);

    Optional<ParticipationRequestDto> rejectRequestPublic(Long userId, Long eventId, Long reqId);

    Optional<List<ParticipationRequestDto>> readRequests(Long userId);

    Optional<ParticipationRequestDto> createRequestPublic(Long userId, Long eventId);
}
