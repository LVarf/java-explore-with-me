package ru.practicum.ewmcore.service.reportService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.reports.ReportDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

@Transactional
public interface ReportInternalService {

    @Transactional(readOnly = true)
    Page<ReportDto> readAllReports(ClientFilter filter, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<ReportDto> readReport(Long reportId);

    Optional<ReportDto> updateReport(Long reportId, ReportDto reportDto);
}
