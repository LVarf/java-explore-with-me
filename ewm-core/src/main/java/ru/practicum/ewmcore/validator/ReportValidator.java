package ru.practicum.ewmcore.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.reports.ReportEntityEnum;
import ru.practicum.ewmcore.repository.ReportRepository;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportValidator extends AbstractValidation {

    private final TimeUtils timeUtils;
    private final ReportRepository repository;

    public void assertValidator(Boolean bool, String classType, String message) {
        assertValidator(bool, classType, message, timeUtils);
    }

    public void validationText(String text) {
        validationSpacesInStringField(text);
        if (text == null || text.isBlank() || text.isEmpty()) {
            assertValidator(true, "CommentServiceImpl", "Поле с текстом пустое");
        }
    }

    public void validateOnDuplicates(ReportEntityEnum entity, Long entityId) {
        final var reportFromDb = repository.findByEntityAndEntityIdAndActual(entity, entityId, true);
        assertValidator(reportFromDb.isPresent(), this.getClass().getSimpleName(),
                "You can create only one report");
    }
}
