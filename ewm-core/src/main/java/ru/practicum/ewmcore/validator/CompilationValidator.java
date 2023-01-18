package ru.practicum.ewmcore.validator;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompilationValidator extends AbstractValidation {
    private final TimeUtils timeUtils;

    public void assertValidator(Boolean bool, String classType, String message) {
        assertValidator(bool, classType, message, timeUtils);
    }
}
