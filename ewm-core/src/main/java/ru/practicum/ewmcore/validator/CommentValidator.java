package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.error.ApiError;
import ru.practicum.ewmcore.repository.CommentRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentValidator extends AbstractValidation {
    private final TimeUtils timeUtils;
    private final CommentRepository commentRepository;

    public void assertValidator(Boolean bool, String classType, String message) {
        assertValidator(bool, classType, message, timeUtils);
    }

    public void validationText(String text) {
        validationSpacesInStringField(text);
        if (text == null || text.isBlank() || text.isEmpty()) {
            assertValidator(true, "CommentServiceImpl", "Поле с текстом пустое");
        }
    }

    public void validationOnExist(Long comId) {
        final var comment = commentRepository.findById(comId);
        if (comment.isEmpty()) {
            final var apiError = new ApiError();
            log.info("Error in {}, comment not found", "CommentValidator");
            apiError.setMessage("Comment not found")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setReason("The required format is not allowed.")
                    .setTimestamp(timeUtils.timestampToString(Timestamp.valueOf(LocalDateTime.now())));
            throw apiError;
        }
    }
}
