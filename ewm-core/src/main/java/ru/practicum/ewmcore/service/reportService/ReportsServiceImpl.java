package ru.practicum.ewmcore.service.reportService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.ReportDtoConverter;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.commentReports.Report;
import ru.practicum.ewmcore.model.commentReports.ReportDto;
import ru.practicum.ewmcore.model.commentReports.ReportEntityEnum;
import ru.practicum.ewmcore.repository.CommentRepository;
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.repository.ReportRepository;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.userService.UserServiceImpl;
import ru.practicum.ewmcore.specification.ReportSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.CommentValidator;
import ru.practicum.ewmcore.validator.EventDtoValidator;
import ru.practicum.ewmcore.validator.ReportValidator;
import ru.practicum.ewmcore.validator.UserValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportsServiceImpl implements ReportInternalService, ReportPublicService{
    private static final String REPORT_CREATE = "Жалоба создана.";
    private final ReportRepository repository;
    private final ReportDtoConverter converter;
    private final ReportValidator validator;
    private final UserValidator userValidator;
    private final UserServiceImpl userService;
    private final TimeUtils timeUtils;
    private final CommentValidator commentValidator;
    private final CommentRepository commentRepository;
    private final CommentInternalService commentInternalService;
    private final EventDtoValidator eventDtoValidator;
    private final EventRepository eventRepository;
    private final EventInternalService eventInternalService;
    private final ReportSpecification specification;

    @Override
    public Page<ReportDto> readAllReports(ClientFilter filter, Pageable pageable) {
        final var reportsFromDb = repository.findAll(specification.findAllSpecification(filter), pageable);
        return reportsFromDb.map(converter::convertFromEntity);
    }

    @Override
    public String createCommentReportPublic(Long userId, Long comId, String text) {
        createReportImpl(userId, text, comId, null, null);
        return REPORT_CREATE;
    }

    @Override
    public String createEventReportPublic(Long userId, Long eventId, String text) {
        createReportImpl(userId, text, null, eventId, null);
        return REPORT_CREATE;
    }

    @Override
    public String createUserReportPublic(Long userId, Long goalId, String text) {
        createReportImpl(userId, text, null, null, goalId);
        return REPORT_CREATE;
    }

    private Report createReportImpl(Long userId, String text, Long comId, Long eventId, Long goalId) {
        validator.validationText(text);
        final var userCreater = userService.findUserByIdInternalImpl(userId);
        userValidator.assertValidator(userCreater.isEmpty(), this.getClass().getSimpleName(),
                "User creater a report is not registered.");
        final var reportForSave = new Report().setText(text)
                .setReportOwner(userCreater.orElseThrow())
                .setActual(true)
                .setCreateDate(timeUtils.now());
        if (comId != null) {
            commentValidator.validationOnExist(comId);
            reportForSave.setEntity(ReportEntityEnum.COMMENT);
            reportForSave.setReportGoalUser(commentRepository.findById(comId).orElseThrow().getCommentOwner());
            reportForSave.setEntityId(comId);
        }
        if (eventId != null ) {
            eventDtoValidator.validationOnExistById(eventId);
            reportForSave.setEntity(ReportEntityEnum.EVENT);
            reportForSave.setReportGoalUser(eventRepository.findById(eventId).orElseThrow().getInitiator());
            reportForSave.setEntityId(eventId);
        }
        if (goalId != null) {
            final var userGoal = userService.findUserByIdInternalImpl(goalId);
            userValidator.assertValidator(userGoal.isEmpty(), this.getClass().getSimpleName(),
                    "Report user goal is not registered.");
            reportForSave.setEntity(ReportEntityEnum.USER);
            reportForSave.setEntityId(userGoal.orElseThrow().getId());
            reportForSave.setReportGoalUser(userGoal.orElseThrow());
        }
        validator.validateOnDuplicates(reportForSave.getEntity(), reportForSave.getEntityId());
        return repository.save(reportForSave);
    }
}
