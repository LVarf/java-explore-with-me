package ru.practicum.ewmcore.service.participationRequest;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.converter.ParticipationRequestDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestStateEnum;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.repository.ParticipationRequestRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.validator.ParticipationRequestDtoValidator;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestInternalService {
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestDtoConverter requestConverter;
    private final ParticipationRequestDtoValidator requestValidator;
    private final EventInternalService eventService;
    private final EventFullDtoConverter eventFullDtoConverter;

    @Override
    public Optional<ParticipationRequestDto> findRequestUserOnEvent(Long userId, Long eventId) {
        final var requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        return requests.map(requestConverter::convertFromEntity);
    }

    @Override
    public Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId) {
        final var eventFromDb = eventService.readEventByInitiator(userId, eventId).orElseThrow();
        final var requestFromDb = requestRepository.findById(reqId).orElse(null);
        requestValidator.validationOnConfirm(eventId, requestFromDb);
        if (!eventFromDb.isRequestModeration() || eventFromDb.getParticipantLimit() == 0) {
            updateRequestToConfirm(eventFromDb, requestFromDb);
            return Optional.of(requestConverter.convertFromEntity(requestFromDb));
        }
        requestValidator.validationOnLimit(eventFromDb.getParticipantLimit(), eventFromDb.getConfirmedRequests());
        updateRequestToConfirm(eventFromDb, requestFromDb);
        if (eventFromDb.getConfirmedRequests().equals(eventFromDb.getParticipantLimit())) {
            final List<ParticipationRequest> requestsOnReject = requestRepository
                    .findByStatus(ParticipationRequestStateEnum.PENDING);
            for (ParticipationRequest request : requestsOnReject) {
                request.setStatus(ParticipationRequestStateEnum.REJECTED);
                requestRepository.save(request);
            }
        }
        return Optional.of(requestConverter.convertFromEntity(requestFromDb));
    }

    @Override
    public Optional<ParticipationRequestDto> rejectRequest(Long userId, Long eventId, Long reqId) {
        eventService.readEventByInitiator(userId, eventId).orElseThrow();
        final var requestFromDb = requestRepository.findById(reqId).orElse(null);
        requestValidator.validationOnConfirm(eventId, requestFromDb);
        requestFromDb.setStatus(ParticipationRequestStateEnum.REJECTED);
        requestRepository.save(requestFromDb);
        return Optional.of(requestConverter.convertFromEntity(requestFromDb));
    }

    @Override
    public Optional<List<ParticipationRequestDto>> readRequestsByRequesterId(Long requesterId) {
        final var requests = requestRepository.findByRequester(requesterId);
        return Optional.of(requests.stream().map(requestConverter::convertFromEntity).collect(Collectors.toList()));
    }

    @Override
    public Optional<ParticipationRequestDto> createRequest(Long userId, Long eventId) {
        final var eventFromDb = eventService.readEvent(eventId);
        requestValidator.validationOnCreate(userId, eventFromDb.orElseThrow());
        requestValidator.validationOnRepeatedRequests(findRequestUserOnEvent(userId, eventId).orElse(null));
        final ParticipationRequest request = new ParticipationRequest()
                //TODO: after implements userService fix return user
                .setRequester(new User().setId(userId).setName("name").setEmail("email@email.ya"))
                .setCreated(Timestamp.from(Instant.now()))
                .setEvent(eventFromDb.map(eventFullDtoConverter::convertToEntity).orElseThrow())
                .setStatus(ParticipationRequestStateEnum.PENDING);
        if (eventFromDb.get().isRequestModeration()) {
            request.setStatus(ParticipationRequestStateEnum.CONFIRMED);
        }
        final var requestFromDb = requestRepository.save(request);
        return Optional.of(requestFromDb).map(requestConverter::convertFromEntity);
    }

    @Override
    public Optional<ParticipationRequestDto> updateRequestCansel(Long userId, Long reqId) {
        final var request = requestRepository.findById(reqId).orElse(null);
        requestValidator.validationOnCansel(userId, request);
        request.setStatus(ParticipationRequestStateEnum.CANCELED);
        return Optional.of(requestRepository.save(request)).map(requestConverter::convertFromEntity);
    }

    private void updateRequestToConfirm(EventFullDto eventFromDb, ParticipationRequest requestFromDb) {
        requestFromDb.setStatus(ParticipationRequestStateEnum.CONFIRMED);
        requestRepository.save(requestFromDb);
        eventFromDb.setConfirmedRequests(eventFromDb.getConfirmedRequests() + 1);
        eventService.updateEvent(eventFromDb);
    }


}
