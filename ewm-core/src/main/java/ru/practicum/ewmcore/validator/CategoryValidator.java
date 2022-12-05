package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.model.category.CategoryDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CategoryValidator extends AbstractValidation {

    private final TimeUtils timeUtils;
    public void validationNameCategory(CategoryDto categoryDto) {
        validationSpacesInStringField(categoryDto.getName());
    }

    public void validationOnExist(CategoryDto categoryDto) {
        final var apiError = new ApiError();
        if (categoryDto == null) {
            log.info("The category {} is not exist", categoryDto.getId());
            apiError.setMessage("The category is not exist")
                    .setStatus(HttpStatus.FORBIDDEN)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
