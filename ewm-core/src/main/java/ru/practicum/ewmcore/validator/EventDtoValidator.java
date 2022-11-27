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

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventDtoValidator {
    private final TimeUtils timeUtils;
    private CategoryInternalService categoryInternalService;

    public void validateOnRead(EventShortDto eventShortDto, ValidationMode validationMode) {
        final var apiError = new ApiError();
        // validateCategoriesExist(eventShortDto.getCategory(), apiError);
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

    public void validationOnUpdate(EventFullDto event, Long userId) {
        final var apiError = new ApiError();
        checkInitiatorIdOnUpdate(event, userId, apiError);
        checkStateOnUpdate(event, apiError);
        checkTimeOnUpdate(event, apiError);
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

    private void checkInitiatorIdOnUpdate(EventFullDto event, Long userId, ApiError apiError) {
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

    private void checkTimeOnUpdate(EventFullDto event, ApiError apiError) {
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
}
