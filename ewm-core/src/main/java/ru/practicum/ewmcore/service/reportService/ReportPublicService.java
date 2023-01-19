package ru.practicum.ewmcore.service.reportService;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ReportPublicService {
    String createCommentReportPublic(Long userId, Long comId, String text);
    String createEventReportPublic(Long userId, Long eventId, String text);
    String createUserReportPublic(Long userId, Long goalId, String text);
}
