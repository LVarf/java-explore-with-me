package ru.practicum.ewmcore.service.reportService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewmcore.model.reports.Report;
import ru.practicum.ewmcore.model.reports.ReportEntityEnum;
import ru.practicum.ewmcore.repository.ReportRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;


@ExtendWith(MockitoExtension.class)
class ReportsServiceImplTest {

    @Mock
    private ReportRepository repository;
    private Report report;

    @BeforeEach
    void setUp() {
        report = new Report().setId(1L)
                .setEntity(ReportEntityEnum.COMMENT)
                .setActual(true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readAllReports() {
    }

    @Test
    void readReport() {
        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(report));
        Report newReport = repository.findById(1L).orElse(null);
        assertNotNull(newReport);
        assertEquals(newReport.getId(), report.getId());
        assertEquals(newReport.getActual(), report.getActual());
        assertEquals(newReport.getEntity(), report.getEntity());
    }

    @Test
    void updateReport() {
    }

    @Test
    void createCommentReportPublic() {
    }

    @Test
    void createEventReportPublic() {
    }

    @Test
    void createUserReportPublic() {
    }
}