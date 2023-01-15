package ru.practicum.ewmcore.service.reportService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.commentReports.ReportDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

public interface ReportInternalService {
    Page<ReportDto> readAllReports(ClientFilter filter, Pageable pageable);
}
