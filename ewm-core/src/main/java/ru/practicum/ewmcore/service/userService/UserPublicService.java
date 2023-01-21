package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;

import java.util.List;
import java.util.Optional;

public interface UserPublicService {

    Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable);

    Optional<EventFullDto> readEventPublic(Long userId, Long eventId);

    Optional<EventFullDto> updateEventPublic(Long id, EventFullDto event);

    Optional<EventFullDto> createEventPublic(Long userId, EventFullDto event);

    Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId);

    List<ParticipationRequestDto> readRequestPublic(Long userId, Long eventId);

    Optional<ParticipationRequestDto> confirmRequestPublic(Long userId, Long eventId, Long reqId);

    Optional<ParticipationRequestDto> rejectRequestPublic(Long userId, Long eventId, Long reqId);

    Optional<List<ParticipationRequestDto>> readRequests(Long userId);

    Optional<ParticipationRequestDto> createRequestPublic(Long userId, Long eventId);

    Optional<ParticipationRequestDto> updateRequestCanselPublic(Long userId, Long requestsId);
}
