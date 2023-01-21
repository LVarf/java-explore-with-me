package ru.practicum.ewmcore.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.reports.Report;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

@Component
@RequiredArgsConstructor
public class ReportSpecification extends AbstractSpecification<Report> {
    public Specification<Report> findAllSpecification(final ClientFilter filters) {
        return ((root, query, criteriaBuilder) ->
                buildFinalPredicate(root, criteriaBuilder, filters)
        );
    }
}
