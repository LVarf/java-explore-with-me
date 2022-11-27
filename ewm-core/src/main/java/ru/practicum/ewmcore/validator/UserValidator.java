package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.repository.UserRepository;

import javax.validation.ValidationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

    private final TimeUtils timeUtils;
    private UserRepository userRepository;
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
}
