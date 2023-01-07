package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.repository.UserRepository;

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator extends AbstractValidation {

    private final TimeUtils timeUtils;
    private final UserRepository userRepository;

    public void validateOnRead(Long userId, ValidationMode validationMode)
            throws ValidationException {
        final var apiError = new ApiError();
        validateUserExist(userId, apiError);
    }

    private void validateUserExist(Long userId, ApiError apiError) {
        final User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.info("User id: {} not found", userId);
            apiError.setMessage("The user is not found")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("The required object was not found.")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationOnSave(UserFullDto userFullDto) {
        final var apiError = new ApiError();
        if (userFullDto.getName() == null) {
            log.info("User {} format is not allowed", userFullDto);
            apiError.setMessage("The user format is not allowed. Name is null.")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("The required format is not allowed.")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationUniqueName(Boolean isError) {
        final var apiError = new ApiError();
        if (isError) {
            log.info("The user name is not unique.");
            apiError.setMessage("The user name is not unique")
                    .setStatus(HttpStatus.CONFLICT)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void assertValidator(Boolean bool, String classType) {
        assertValidator(bool, classType, timeUtils);
    }
}
