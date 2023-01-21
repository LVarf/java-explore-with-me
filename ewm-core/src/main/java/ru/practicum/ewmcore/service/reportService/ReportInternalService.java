package ru.practicum.ewmcore.service.reportService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.reports.ReportDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface ReportInternalService {

    Page<ReportDto> readAllReports(ClientFilter filter, Pageable pageable);

    Optional<ReportDto> readReport(Long reportId);

    Optional<ReportDto> updateReport(Long reportId, ReportDto reportDto);
}
