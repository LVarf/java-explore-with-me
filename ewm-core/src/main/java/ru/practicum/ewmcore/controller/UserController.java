package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
        return userService.readAllPublic(userId, pageable);
    }

    @PatchMapping("/events")
    public Optional<EventShortDto> updateAllEvents(@PathVariable Long userId) {
        return Optional.empty();
    }

    @PutMapping("/events")
    public Optional<EventFullDto> createAllEvents(@PathVariable Long userId,
                                                  @RequestBody EventFullDto event) {
        return Optional.of(event);
    }

    @GetMapping("/events/{eventId}")
    public Page<EventFullDto> readEvent(@PathVariable Long userId,
                                        @PathVariable Long eventId) {
        return Page.empty();
    }

    @PatchMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEvent(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        return Optional.empty();
    }

    @GetMapping("/events/{eventId}/requests")
    public Optional<ParticipationRequestDto> readRequests(@PathVariable Long userId,
                                                          @PathVariable Long eventId) {
        return Optional.empty();
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public Optional<ParticipationRequestDto> updateRequestsConfirm(@PathVariable Long userId,
                                                                   @PathVariable Long eventId,
                                                                   @PathVariable Long reqId) {
        return Optional.empty();
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public Optional<ParticipationRequestDto> updateRequests(@PathVariable Long userId,
                                                            @PathVariable Long eventId,
                                                            @PathVariable Long reqId) {
        return Optional.empty();
    }

    @GetMapping("/requests")
    public Optional<ParticipationRequestDto> readRequest(@PathVariable Long userId) {
        return Optional.empty();
    }

    @PatchMapping("/requests/{requestsId}/cansel")
    public Optional<ParticipationRequestDto> updateRequest(@PathVariable Long userId,
                                                           @PathVariable Long requestsId) {
        return Optional.empty();
    }

    @PutMapping("/requests")
    public Optional<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                           @RequestParam(value = "eventId") Long eventId) {
        return Optional.empty();
    }
}
