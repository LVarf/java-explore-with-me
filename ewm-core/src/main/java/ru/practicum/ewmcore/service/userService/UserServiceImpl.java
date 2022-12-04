package ru.practicum.ewmcore.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.model.user.UserShortDto;
import ru.practicum.ewmcore.repository.UserRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.participationRequest.ParticipationRequestInternalService;
import ru.practicum.ewmcore.validator.UserValidator;
import ru.practicum.ewmcore.validator.ValidationMode;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserInternalService, UserPublicService {
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final EventInternalService eventInternalService;
    private final ParticipationRequestInternalService requestInternalService;
    private final EventShortDtoConverter eventShortDtoConverter;

    @Override
    public Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable) {
        log.debug("Public read all events by user id {}", userId);
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.readAllByInitiatorId(userId, pageable);
    }

    @Override
    public Optional<EventFullDto> updateEventPublic(Long userId, EventFullDto event) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.updateEventByUser(userId, event);
    }

    public Optional<EventFullDto> createEventPublic(Long userId, EventFullDto event) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.createEvent(event);
    }

    private List<UserShortDto> readShortImpl(Long userId) {
        return null;
    }


    @Override
    public Optional<EventFullDto> readEventPublic(Long userId, Long eventId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.readEventByInitiator(userId, eventId);
    }


    @Override
    public Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.updateEventOnCancel(userId, eventId);
    }

    @Override
    public Optional<ParticipationRequestDto> readRequestPublic(Long userId, Long eventId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.findRequestUserOnEvent(userId, eventId);
    }

    @Override
    public Optional<ParticipationRequestDto> confirmRequestPublic(Long userId, Long eventId, Long reqId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.confirmRequest(userId, eventId, reqId);
    }

    @Override
    public Optional<ParticipationRequestDto> rejectRequestPublic(Long userId, Long eventId, Long reqId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.rejectRequest(userId, eventId, reqId);
    }

    @Override
    public Optional<List<ParticipationRequestDto>> readRequests(Long userId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.readRequestsByRequesterId(userId);
    }

    @Override
    public Optional<ParticipationRequestDto> createRequestPublic(Long userId, Long eventId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.createRequest(userId, eventId);
    }

    @Override
    public Optional<ParticipationRequestDto> updateRequestCanselPublic(Long userId, Long requestsId) {
        userValidator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.updateRequestCansel(userId, requestsId);
    }

    /*private Page<UserFullDto> enrichPage(Pageable pageable, Page<UserFullDto> repositoryPage) {
        final var page = pageConverter.convertToPage(repositoryPage, pageable);
        final List<UserFullDto> pageContent = page.first.stream()
                .map(converter::convertFromEntity)
                .map(directoryItemDto -> enrichVersion(directoryItemDto, validFrom))
                .filter(Objects::nonNull).map(this::enrichDirectory).map(this::enrichAttributes)
                .collect(Collectors.toList());
        return new PageImpl<>(pageContent, page.second, repositoryPage.getTotalElements());
    }*/
}
