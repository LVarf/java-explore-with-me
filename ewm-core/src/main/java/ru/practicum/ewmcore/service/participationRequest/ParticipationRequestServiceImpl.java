package ru.practicum.ewmcore.service.participationRequest;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.ParticipationRequestDtoConverter;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestStateEnum;
import ru.practicum.ewmcore.repository.ParticipationRequestRepository;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.validator.ParticipationRequestDtoValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestInternalService {
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestDtoConverter requestConverter;
    private final ParticipationRequestDtoValidator requestValidator;
    private final EventInternalService eventService;

    @Override
    public Optional<ParticipationRequestDto> findRequestUserOnEvent(Long userId, Long eventId) {
        final var requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        return requests.map(requestConverter::convertFromEntity);
    }

    @Override
    public Optional<ParticipationRequestDto> confirmRequest(Long userId, Long eventId, Long reqId) {
        final var eventFromDb = eventService.readEvent(userId, eventId).orElseThrow();
        final var requestFromDb = requestRepository.findById(reqId).orElse(null);
        requestValidator.validationOnConfirm(eventId, requestFromDb);
        if (!eventFromDb.isRequestModeration() || eventFromDb.getParticipantLimit() == 0) {
            updateRequestToConfirm(eventFromDb, requestFromDb);
            return Optional.of(requestConverter.convertFromEntity(requestFromDb));
        }
        requestValidator.validationOnLimit(eventFromDb.getParticipantLimit(), eventFromDb.getConfirmedRequests());
        updateRequestToConfirm(eventFromDb, requestFromDb);
        if (eventFromDb.getConfirmedRequests() == eventFromDb.getParticipantLimit()) {
            final List<ParticipationRequest> requestsOnReject = requestRepository
                    .findByStatus(ParticipationRequestStateEnum.PENDING);
            for(ParticipationRequest request: requestsOnReject) {
                request.setStatus(ParticipationRequestStateEnum.REJECTED);
                requestRepository.save(request);
            }
        }
        return Optional.of(requestConverter.convertFromEntity(requestFromDb));
    }

    private void updateRequestToConfirm(EventFullDto eventFromDb, ParticipationRequest requestFromDb) {
        requestFromDb.setStatus(ParticipationRequestStateEnum.CONFIRMED);
        requestRepository.save(requestFromDb);
        eventFromDb.setConfirmedRequests(eventFromDb.getConfirmedRequests() + 1);
        eventService.updateEvent(eventFromDb);
    }


}
