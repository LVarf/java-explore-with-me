package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractValidation {
    protected void validationSpacesInStringField(String field) {
        field = field != null ? field.trim() : null;
    }

    public void assertValidator(Boolean bool, String classType, TimeUtils timeUtils) {
        if (bool) {
            final var apiError = new ApiError();
            log.info("Error in {}", classType);
            apiError.setMessage("Error in " + classType)
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("The required format is not allowed.")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
