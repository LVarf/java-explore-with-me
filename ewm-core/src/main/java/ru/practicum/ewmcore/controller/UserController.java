package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.service.userService.UserPublicService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}")
public class UserController {
    private final UserPublicService userService;

    @GetMapping("/events")
    public Page<EventShortDto> readAllEvents(@PathVariable Long userId,
                                             @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC,
                                                     page = 0, size = 10) Pageable pageable) {
        return userService.readAllEventsPublic(userId, pageable);
    }

    @PatchMapping("/events")
    public Optional<EventFullDto> updateEvent(@PathVariable Long userId,
                                              @RequestBody EventFullDto event) {
        return userService.updateEventPublic(userId, event);
    }

    @PostMapping("/events")
    public Optional<EventFullDto> createEvent(@PathVariable Long userId,
                                              @RequestBody EventFullDto event) {
        return userService.createEventPublic(userId, event);
    }

    @GetMapping("/events/{eventId}")
    public Optional<EventFullDto> readEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        return userService.readEventPublic(userId, eventId);
    }

    @PatchMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEventOnCancel(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        return userService.updateEventOnCancel(userId, eventId);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> readRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return userService.readRequestPublic(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public Optional<ParticipationRequestDto> updateRequestConfirm(@PathVariable Long userId,
                                                                   @PathVariable Long eventId,
                                                                   @PathVariable Long reqId) {
        return userService.confirmRequestPublic(userId, eventId, reqId);
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public Optional<ParticipationRequestDto> updateRequestReject(@PathVariable Long userId,
                                                            @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        return userService.rejectRequestPublic(userId, eventId, reqId);
    }

    @GetMapping("/requests")
    public Optional<List<ParticipationRequestDto>> readRequests(@PathVariable Long userId) {
        return userService.readRequests(userId);
    }

    @PostMapping("/requests")
    public Optional<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                           @RequestParam(value = "eventId") Long eventId) {
        return userService.createRequestPublic(userId, eventId);
    }

    @PatchMapping("/requests/{requestsId}/cancel")
    public Optional<ParticipationRequestDto> updateRequestCansel(@PathVariable Long userId,
                                                           @PathVariable Long requestsId) {
        return userService.updateRequestCanselPublic(userId, requestsId);
    }
}
