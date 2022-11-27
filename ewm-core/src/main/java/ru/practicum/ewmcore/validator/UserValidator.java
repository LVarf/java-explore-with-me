package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.repository.UserRepository;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

    private final static String CATEGORY_NOT_FOUND = "Categories not found";
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
            apiError.setMessage("Categories are not found");
            throw apiError;
        }
    }
}
