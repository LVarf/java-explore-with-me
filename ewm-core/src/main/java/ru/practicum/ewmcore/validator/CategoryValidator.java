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

    public void validationOnResponse(CategoryDto categoryDto) {
        final var apiError = new ApiError();
        if (categoryDto == null) {
            log.info("The category is not found");
            apiError.setMessage("The category is not found")
                    .setStatus(HttpStatus.NOT_FOUND)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationCategoryOnUpdate(CategoryDto categoryDto) {
        final var apiError = new ApiError();
        if (categoryDto.getId() == null || categoryDto.getName() == null) {
            log.info("The category format {} is not allowed", categoryDto);
            apiError.setMessage("The category format is not allowed")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationUniqueName(Boolean isError) {
        final var apiError = new ApiError();
        if (isError) {
            log.info("The category name is not unique.");
            apiError.setMessage("The category name is not unique")
                    .setStatus(HttpStatus.CONFLICT)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }

    public void validationCategoryOnSave(CategoryDto categoryDto) {
        final var apiError = new ApiError();
        if (categoryDto.getName() == null) {
            log.info("The category format {} is not allowed. Name is null.", categoryDto);
            apiError.setMessage("The category format is not allowed. Name is null.")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("For the requested operation the conditions are not met")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
