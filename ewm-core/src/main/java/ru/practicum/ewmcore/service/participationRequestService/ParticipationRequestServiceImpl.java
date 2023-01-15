package ru.practicum.ewmcore.service.participationRequestService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventFullDtoConverter;
import ru.practicum.ewmcore.converter.ParticipationRequestDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestStateEnum;
import ru.practicum.ewmcore.repository.ParticipationRequestRepository;
import ru.practicum.ewmcore.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public Optional<ParticipationRequestDto> findRequestRequesterOnEvent(Long userId, Long eventId) {
        return requestRepository.findByRequesterIdAndEventId(userId, eventId).map(requestConverter::convertFromEntity);
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserAndEvent(Long eventId) {
        return requestRepository.findByEventId(eventId)
                .stream().map(requestConverter::convertFromEntity).collect(Collectors.toList());
    }

    @Override
    public Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId) {
        final var eventFromDb = eventService.readEventByInitiator(userId, eventId).orElseThrow();
        final var requestFromDb = readParticipationRequestImpl(eventId, reqId).orElseThrow();
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
        final var requestFromDb = readParticipationRequestImpl(eventId, reqId).orElseThrow();
        requestFromDb.setStatus(ParticipationRequestStateEnum.REJECTED);
        requestRepository.save(requestFromDb);
        return Optional.of(requestConverter.convertFromEntity(requestFromDb));
    }

    @Override
    public Optional<List<ParticipationRequestDto>> readRequestsByRequesterId(Long requesterId) {
        final var requests = requestRepository.findAllByRequesterId(requesterId);
        return Optional.of(requests.stream().map(requestConverter::convertFromEntity).collect(Collectors.toList()));
    }

    @Override
    public Optional<ParticipationRequestDto> createRequest(Long userId, Long eventId) {
        final var eventFromDb = eventService.readEvent(eventId);
        requestValidator.validationOnCreate(userId, eventFromDb.orElseThrow());
        requestValidator.validationOnRepeatedRequests(findRequestRequesterOnEvent(userId, eventId).orElse(null));
        final ParticipationRequest request = new ParticipationRequest()
                .setRequester(userRepository.findUserById(userId).orElseThrow())
                .setCreated(Timestamp.from(Instant.now()))
                .setEvent(eventFromDb.map(eventFullDtoConverter::convertToEntity).orElseThrow())
                .setStatus(ParticipationRequestStateEnum.PENDING);
        if (!eventFromDb.get().isRequestModeration()) {
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
        eventService.updateEvent(eventFullDtoConverter.convertToEntity(eventFromDb));
    }

    private Optional<ParticipationRequest> readParticipationRequestImpl(Long eventId, Long reqId) {
        final var requestFromDb = requestRepository.findById(reqId);
        requestValidator.validationOnConfirm(eventId, requestFromDb.orElse(null));
        return requestFromDb;
    }
}
