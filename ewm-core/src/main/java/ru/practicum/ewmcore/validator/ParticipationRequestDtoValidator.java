package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;

import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    public void validationOnLimit(Integer limit, Integer requests) {
        final var apiError = new ApiError();
        if (requests >= limit) {
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
        if (request.getEvent().getId() != eventId) {
            log.info("Event ids are not matched");
            apiError.setMessage("Event ids are not matched")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
