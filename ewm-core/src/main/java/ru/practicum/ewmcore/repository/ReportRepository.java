package ru.practicum.ewmcore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.commentReports.Report;
import ru.practicum.ewmcore.model.commentReports.ReportEntityEnum;

import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByEntityAndEntityIdAndActual(ReportEntityEnum entity, Long entityId, Boolean actual);

    Page<Report> findAll(Specification<Report> specification, Pageable pageable);
}
