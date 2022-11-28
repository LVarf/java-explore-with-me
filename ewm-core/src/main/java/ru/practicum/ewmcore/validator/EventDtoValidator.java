package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDtoValidator extends AbstractValidation {
    private final TimeUtils timeUtils;
    private CategoryInternalService categoryInternalService;

    public void validationOnRead(Long userId, EventFullDto event) {
        final var apiError = new ApiError();
        checkInitiatorIdEqualsUserId(userId, event, apiError);
        // validateCategoriesExist(eventShortDto.getCategory(), apiError);
    }

    public void validationOnCancel(EventFullDto event) {
        final var apiError = new ApiError();
        checkEventPendingState(event, apiError);
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

    private void validateCategoriesExist(long[] categories, ApiError apiError) {
        final List<CategoryDto> categoryDtoList = new ArrayList<>();
        for (long id : categories) {
            final var category = categoryInternalService.readInternal(id).orElse(null);
            if (category != null) {
                categoryDtoList.add(category);
            }
        }
        if (categoryDtoList.isEmpty()) {
            log.info("Categories id: {} not found", Arrays.toString(categories));
            apiError.setMessage("Categories are not found")
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setReason("The required object was not found.")
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
        checkTime(event, apiError);
        //checkFieldsNotNull
        //checkLengthRequired
        validationSpaces(event, null);
    }

    private void validationSpaces(EventFullDto eventFull, EventShortDto eventShort) {
        if (eventFull != null) {
            validationSpacesInStringField(eventFull.getAnnotation());
            validationSpacesInStringField(eventFull.getTitle());
            validationSpacesInStringField(eventFull.getDescription());
        }
    }
}
