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
import ru.practicum.ewmcore.repository.EventRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDtoValidator extends AbstractValidation {
    private final TimeUtils timeUtils;
    private final EventRepository eventRepository;

    public void validationOnRead(Long userId, EventFullDto event) {
        final var apiError = new ApiError();
        checkInitiatorIdEqualsUserId(userId, event, apiError);
    }

    public void validationOnCancel(EventFullDto event) {
        final var apiError = new ApiError();
        checkEventPendingState(event, apiError);
    }

    public void validationOnPublished(EventFullDto event) {
        final var apiError = new ApiError();
        checkTime(event, apiError);
        checkEventPendingState(event, apiError);
    }

    public void validationOnReject(EventFullDto event) {
        final var apiError = new ApiError();
        checkEventPublishedState(event, apiError);
    }

    private void checkEventPendingState(EventFullDto event, ApiError apiError) {
        if (event.getState() != null && event.getState() != EventStateEnum.PENDING) {
            log.info("The event {} state is {}. It is not PENDING", event.getId(), event.getState());
            apiError.setMessage("The event state is not PENDING")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    private void checkEventPublishedState(EventFullDto event, ApiError apiError) {
        if (event.getState() != null && event.getState().equals(EventStateEnum.PUBLISHED)) {
            log.info("The event {} state is PUBLISHED", event.getId());
            apiError.setMessage("The event state is PUBLISHED")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnUpdate(Long userId, EventFullDto event) {
        final var apiError = new ApiError();
        checkInitiatorIdEqualsUserId(userId, event, apiError);
        checkStateOnUpdate(event, apiError);
        checkTime(event, apiError);
    }

    public void validationOnExist(Event event) {
        final var apiError = new ApiError();
        if (event == null) {
            log.info("The event not found");
            apiError.setMessage("The event not found")
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnExistById(Long id) {
        final var eventFromDb = eventRepository.findById(id);
        validationOnExist(eventFromDb.orElse(null));
    }

    private void checkInitiatorIdEqualsUserId(Long userId, EventFullDto event, ApiError apiError) {
        if (!event.getInitiator().getId().equals(userId)) {
            log.info("The user {} is not initiator the event {}", userId, event.getId());
            apiError.setMessage("The user is not initiator the event")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    private void checkStateOnUpdate(EventFullDto event, ApiError apiError) {
        final var state = event.getState();
        if (!(state.equals(EventStateEnum.CANCELED) || state.equals(EventStateEnum.PENDING))) {
            log.info("Event status is not canceled or pending");
            apiError.setMessage("Event status is not canceled or pending")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    private void checkTime(EventFullDto event, ApiError apiError) {
        final var timeEvent = timeUtils.stringToTimestamp(event.getEventDate());
        final var timeNow = Timestamp.valueOf(LocalDateTime.now().plusHours(2));
        if (timeEvent.before(timeNow)) {
            log.info("Event date too early");
            apiError.setMessage("Event date too early")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnCreate(EventFullDto event) {
        final var apiError = new ApiError();
        final var isBadRequest =
                event.getAnnotation() == null || event.getAnnotation().isEmpty() || event.getAnnotation().isBlank() ||
                        event.getDescription() == null || event.getDescription().isEmpty() ||
                        event.getDescription().isBlank() ||
                        event.getTitle() == null || event.getTitle().isEmpty() ||
                        event.getTitle().isBlank() ||
                        event.getCategory() == null ||
                        event.getEventDate() == null ||
                        event.getLocation() == null || event.getLocation().getLat() == null ||
                        event.getLocation().getLon() == null;
        if (isBadRequest) {
            log.info("Event object has bad body");
            apiError.setMessage("Event object has bad body")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
        checkTime(event, apiError);
        validationSpaces(event);
    }

    private void validationSpaces(EventFullDto eventFull) {
        if (eventFull != null) {
            validationSpacesInStringField(eventFull.getAnnotation());
            validationSpacesInStringField(eventFull.getTitle());
            validationSpacesInStringField(eventFull.getDescription());
        }
    }
}
