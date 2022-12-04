package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ParticipationRequestDtoValidator extends AbstractValidation {
    private final TimeUtils timeUtils;
    private CategoryInternalService categoryInternalService;


    public void validationOnConfirm(Long eventId, ParticipationRequest request) {
        final var apiError = new ApiError();
        validationOnExist(request, apiError);
        checkMatchedEventId(eventId, request, apiError);
    }

    public void validationOnCreate(Long userId, EventFullDto event) {
        final var apiError = new ApiError();
        checkInitiatorIsRequester(event.getInitiator().getId(), userId, apiError);
        checkPublished(event, apiError);
        validationOnLimit(event.getParticipantLimit(), event.getConfirmedRequests());
    }

    private void checkPublished(EventFullDto event, ApiError apiError) {
        if(!event.getState().equals(EventStateEnum.PUBLISHED)) {
            log.info("Requester can't made a request if the event is not published {}", event.getId());
            apiError.setMessage("Requester can't made a request if the event is not published")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnRepeatedRequests(ParticipationRequestDto request) {
        if (request != null) {
            final var apiError = new ApiError();
            log.info("Requester can't made more than one request on the same event");
            apiError.setMessage("Requester can't made more than one request on the same event")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    private void checkInitiatorIsRequester(Long initiatorId, Long requesterId, ApiError apiError) {
        if (initiatorId == requesterId) {
            log.info("Initiator can't made request on his own event");
            apiError.setMessage("Initiator can't made request on his own event")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnLimit(Integer limit, Integer requests) {
        final var apiError = new ApiError();
        if (limit != 0 && requests >= limit) {
            log.info("Requests limit reached");
            apiError.setMessage("Requests limit reached")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnExist(ParticipationRequest request, ApiError apiError) {
        if (request == null) {
            log.info("The request not found");
            apiError.setMessage("The request not found")
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    private void checkMatchedEventId(Long eventId,
                                     ParticipationRequest request, ApiError apiError) {
        if (!request.getEvent().getId().equals(eventId)) {
            log.info("Event ids are not matched");
            apiError.setMessage("Event ids are not matched")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
