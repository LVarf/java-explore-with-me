package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.converter.EventFullDtoResponseConverter;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventFullDtoResponse;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;
import ru.practicum.ewmcore.service.userService.UserPublicService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}")
public class UserController {
    private final UserPublicService userService;
    private final EventFullDtoResponseConverter eventResponseConverter;
    private final CommentInternalService commentService;

    @PostMapping("/comments/{eventId}")
    public Optional<CommentDto> createComment(@PathVariable Long eventId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto comment) {
        return commentService.createComment(eventId, userId, comment);
    }

    @PatchMapping("/comments/{comId}")
    public Optional<CommentDto> updateComment(@PathVariable Long comId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto comment) {
        return commentService.updateComment(comId, userId, comment);
    }

    @DeleteMapping("/comments/{comId}")
    public String deleteComment(@PathVariable Long comId,
                                @PathVariable Long userId) {
        return commentService.deleteComment(comId, userId);
    }

    @GetMapping("/events")
    public List<EventShortDto> readAllEvents(@PathVariable Long userId,
                                             @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC)
                                             Pageable pageable) {
        log.info("Input dates UserController.readAllEvents: userId: {}, pageable: {}", userId, pageable);
        final var result = userService.readAllEventsPublic(userId, pageable).toList();
        log.info("Output dates UserController.readAllEvents: result: {}", result);
        return result;
    }

    @PatchMapping("/events")
    public Optional<EventFullDto> updateEvent(@PathVariable Long userId,
                                              @RequestBody EventFullDtoResponse eventResponse) {
        log.info("Input dates UserController.updateEvent: userId: {}, EventFullDtoResponse: {}", userId, eventResponse);
        final var result = userService.updateEventPublic(userId, eventResponseConverter.convertToEntity(eventResponse));
        log.info("Output dates UserController.updateEvent: result: {}", result);
        return result;
    }

    @PostMapping("/events")
    public Optional<EventFullDto> createEvent(@PathVariable Long userId,
                                              @RequestBody EventFullDtoResponse eventResponse) {
        log.info("Input dates UserController.createEvent: userId: {}, EventFullDtoResponse: {}", userId, eventResponse);
        final var result = userService.createEventPublic(userId, eventResponseConverter.convertToEntity(eventResponse));
        log.info("Output dates UserController.createEvent: result: {}", result);
        return result;
    }

    @GetMapping("/events/{eventId}")
    public Optional<EventFullDto> readEvent(@PathVariable Long userId,
                                            @PathVariable Long eventId) {
        log.info("Input dates UserController.readEvent: userId: {}, eventId: {}", userId, eventId);
        final var result = userService.readEventPublic(userId, eventId);
        log.info("Output dates UserController.readEvent: result: {}", result);
        return result;
    }

    @PatchMapping("/events/{eventId}")
    public Optional<EventFullDto> updateEventOnCancel(@PathVariable Long userId,
                                                      @PathVariable Long eventId) {
        log.info("Input dates UserController.updateEventOnCancel: userId: {}, eventId: {}", userId, eventId);
        final var result = userService.updateEventOnCancel(userId, eventId);
        log.info("Output dates UserController.updateEventOnCancel: result: {}", result);
        return result;
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> readRequestsByEventId(@PathVariable Long userId,
                                                               @PathVariable Long eventId) {
        log.info("Input dates UserController.readRequestsByEventId: userId: {}, eventId: {}", userId, eventId);
        final var result = userService.readRequestPublic(userId, eventId);
        log.info("Output dates UserController.readRequestsByEventId: result: {}", result);
        return result;
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/confirm")
    public Optional<ParticipationRequestDto> updateRequestConfirm(@PathVariable Long userId,
                                                                  @PathVariable Long eventId,
                                                                  @PathVariable Long reqId) {
        log.info("Input dates UserController.updateRequestConfirm: userId: {}, eventId: {}, reqId: {}",
                userId, eventId, reqId);
        final var result = userService.confirmRequestPublic(userId, eventId, reqId);
        log.info("Output dates UserController.updateRequestConfirm: result: {}", result);
        return result;
    }

    @PatchMapping("/events/{eventId}/requests/{reqId}/reject")
    public Optional<ParticipationRequestDto> updateRequestReject(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @PathVariable Long reqId) {
        log.info("Input dates UserController.updateRequestReject: userId: {}, eventId: {}, reqId: {}",
                userId, eventId, reqId);
        final var result = userService.rejectRequestPublic(userId, eventId, reqId);
        log.info("Output dates UserController.updateRequestReject: result: {}", result);
        return result;
    }

    @GetMapping("/requests")
    public Optional<List<ParticipationRequestDto>> readRequests(@PathVariable Long userId) {
        log.info("Input dates UserController.readRequests: userId: {}", userId);
        final var result = userService.readRequests(userId);
        log.info("Output dates UserController.readRequests: result: {}", result);
        return result;
    }

    @PostMapping("/requests")
    public Optional<ParticipationRequestDto> createRequest(@PathVariable Long userId,
                                                           @RequestParam(value = "eventId") Long eventId) {
        log.info("Input dates UserController.createRequest: userId: {}, eventId: {}", userId, eventId);
        final var result = userService.createRequestPublic(userId, eventId);
        log.info("Output dates UserController.createRequest: result: {}", result);
        return result;
    }

    @PatchMapping("/requests/{requestsId}/cancel")
    public Optional<ParticipationRequestDto> updateRequestCansel(@PathVariable Long userId,
                                                                 @PathVariable Long requestsId) {
        log.info("Input dates UserController.updateRequestCansel: userId: {}, requestsId: {}", userId, requestsId);
        final var result = userService.updateRequestCanselPublic(userId, requestsId);
        log.info("Output dates UserController.updateRequestCansel: result: {}", result);
        return result;
    }
}
