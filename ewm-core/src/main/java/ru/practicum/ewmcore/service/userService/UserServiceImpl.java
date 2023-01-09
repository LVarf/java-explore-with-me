package ru.practicum.ewmcore.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.UserDtoConverter;
import ru.practicum.ewmcore.converter.UserShortDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.repository.UserRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.participationRequest.ParticipationRequestInternalService;
import ru.practicum.ewmcore.specification.UserSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.UserValidator;
import ru.practicum.ewmcore.validator.ValidationMode;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserInternalService, UserPublicService {
    private static final String USER_IS_DELETED = "Пользователь удалён";
    private final UserRepository repository;
    private final UserValidator validator;
    private final EventInternalService eventInternalService;
    private final ParticipationRequestInternalService requestInternalService;
    private final UserDtoConverter userFullDtoConverter;
    private final UserShortDtoConverter userShortDtoConverter;
    private final UserSpecification specification;

    @Override
    public Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable) {
        log.debug("Public read all events by user id {}", userId);
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.readAllByInitiatorId(userId, pageable);
    }

    @Override
    public Optional<EventFullDto> updateEventPublic(Long userId, EventFullDto event) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.updateEventByUser(userId, event);
    }

    public Optional<EventFullDto> createEventPublic(Long userId, EventFullDto event) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        enrichInitiator(userId, event);
        return eventInternalService.createEvent(event);
    }

    private EventFullDto enrichInitiator(Long userId, EventFullDto eventFullDto) {
        final var initiator = userShortDtoConverter.convertFromEntity(repository.findById(userId).orElseThrow());
        eventFullDto.setInitiator(initiator);
        return eventFullDto;
    }

    @Override
    public Optional<EventFullDto> readEventPublic(Long userId, Long eventId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.readEventByInitiator(userId, eventId);
    }


    @Override
    public Optional<EventFullDto> updateEventOnCancel(Long userId, Long eventId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return eventInternalService.updateEventOnCancel(userId, eventId);
    }

    @Override
    public List<ParticipationRequestDto> readRequestPublic(Long userId, Long eventId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        final var event = eventInternalService.readById(eventId).orElseThrow();
        validator.assertValidator(!event.getInitiator().getId().equals(userId), this.getClass().getName(),
                "Вы не являетесь инициатором события.");
        return requestInternalService.findRequestsByUserAndEvent(eventId);
    }

    @Override
    public Optional<ParticipationRequestDto> confirmRequestPublic(Long userId, Long eventId, Long reqId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.confirmRequest(userId, eventId, reqId);
    }

    @Override
    public Optional<ParticipationRequestDto> rejectRequestPublic(Long userId, Long eventId, Long reqId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.rejectRequest(userId, eventId, reqId);
    }

    @Override
    public Optional<List<ParticipationRequestDto>> readRequests(Long userId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.readRequestsByRequesterId(userId);
    }

    @Override
    public Optional<ParticipationRequestDto> createRequestPublic(Long userId, Long eventId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.createRequest(userId, eventId);
    }

    @Override
    public Optional<ParticipationRequestDto> updateRequestCanselPublic(Long userId, Long requestsId) {
        validator.validateOnRead(userId, ValidationMode.DEFAULT);
        return requestInternalService.updateRequestCansel(userId, requestsId);
    }

    @Override
    public Page<UserFullDto> findAllUsersInternal(ClientFilter filter, Pageable pageable) {
        final var usersFromDb = repository.findAll(specification.findAllSpecification(filter), pageable);
        return usersFromDb.map(userFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<UserFullDto> findUserByIdInternal(Long ids) {
        return findUserByIdInternalImpl(ids).map(userFullDtoConverter::convertFromEntity);
    }

    @Override
    public Optional<User> findUserByIdInternalImpl(Long ids) {
        return repository.findUserById(ids);
    }

    @Override
    public Optional<UserFullDto> createUserInternal(UserFullDto userFullDto) {
        validator.validationOnSave(userFullDto);
        final var isUniqueName = repository.findUserByName(userFullDto.getName()).orElse(null) != null;
        validator.validationUniqueName(isUniqueName);
        final var userFromSave = repository.save(userFullDtoConverter.convertToEntity(userFullDto));
        return Optional.of(userFromSave).map(userFullDtoConverter::convertFromEntity);
    }

    @Override
    public String deleteUserInternal(Long userId) {
        final var userFromDb = repository.findById(userId);
        repository.delete(userFromDb.get());
        return USER_IS_DELETED;
    }
}
