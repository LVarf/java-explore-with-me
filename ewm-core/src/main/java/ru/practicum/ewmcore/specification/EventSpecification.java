package ru.practicum.ewmcore.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventSpecification extends AbstractSpecification<Event> {

    private static final String ANNOTATION = "annotation";
    private static final String DESCRIPTION = "description";

    public Specification<Event> findAllSpecification(final ClientFilter filters) {
        return ((root, query, criteriaBuilder) -> buildFinalPredicate(root, criteriaBuilder, filters));
    }

    public Specification<Event> findAllSpecificationForPublic(final ClientFilter filter) {
        return ((root, query, criteriaBuilder) -> {
            final List<ClientFilterParam> filterText = findTextFilter(filter);
            final var findTextPredicate = buildFindTextPredicate(filterText, root, criteriaBuilder);
            filter.getFilters().add(buildPublishedFilterParam());
            final var predicates = buildFinalPredicate(root, criteriaBuilder, filter);
            if (findTextPredicate != null) {
                return criteriaBuilder.and(predicates, findTextPredicate);
            }
            return predicates;
        });
    }

    private ClientFilterParam buildPublishedFilterParam() {
        return new ClientFilterParam().setProperty("state")
                .setOperator(Comparison.EQ).setMainValue(EventStateEnum.PUBLISHED);
    }

    private List<ClientFilterParam> findTextFilter(ClientFilter filter) {
        final var textFilters = filter.getFilters().stream()
                .filter(filterParam -> filterParam.getProperty() == ANNOTATION ||
                        filterParam.getProperty() == DESCRIPTION)
                .collect(Collectors.toList());
        filter.getFilters().removeAll(textFilters);
        return textFilters;
    }

    private Predicate buildFindTextPredicate(List<ClientFilterParam> filterText, final Root<Event> root,
                                             final CriteriaBuilder criteriaBuilder) {
        if (filterText.size() == 0) {
            return null;
        }
        final List<Predicate> predicates = filterText.stream()
                .map(filter -> buildPredicate(filter, root, criteriaBuilder))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        final Predicate firstPredicate = predicates.get(0) != null ? predicates.get(0) : null;
        final Predicate secondPredicate = predicates.get(1) != null ? predicates.get(0) : null;
        if (firstPredicate != null && secondPredicate != null) {
            return criteriaBuilder.or(firstPredicate, secondPredicate);
        }
        if (firstPredicate != null) {
            return criteriaBuilder.and(firstPredicate);
        }
        if (secondPredicate != null) {
            return criteriaBuilder.and(secondPredicate);
        }
        return null;
    }
}
