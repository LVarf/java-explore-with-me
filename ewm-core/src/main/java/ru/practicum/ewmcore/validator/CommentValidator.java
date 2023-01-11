package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentValidator extends AbstractValidation {

    private final TimeUtils timeUtils;

    public void assertValidator(Boolean bool, String classType) {
        assertValidator(bool, classType, timeUtils);
    }

    public void validationText(String text) {
        validationSpacesInStringField(text);
        if (text == null || text.isBlank() || text.isEmpty()) {
            assertValidator(true, "CommentServiceImpl");
        }
    }
}
