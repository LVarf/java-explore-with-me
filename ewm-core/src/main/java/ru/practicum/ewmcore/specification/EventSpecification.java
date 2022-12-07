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

@Component
@RequiredArgsConstructor
public class EventSpecification extends AbstractSpecification<Event> {

    private static final String SORT_EVENT_DATE = "EVENT_DATE";
    private static final String EVENT_DATE_CONST = "eventDate";

    private static final String CONFIRMED_REQUESTS = "confirmedRequests";
    private static final String PUBLISHED_VALUE_CONST = EventStateEnum.PUBLISHED.toString();

    public Specification<Event> findAllSpecification(final ClientFilter filters) {
        return ((root, query, criteriaBuilder) -> buildFinalPredicate(root, criteriaBuilder, filters));
    }

    public Specification<Event> findAllSpecificationForPublic(final ClientFilter filter, String sort) {
        //фильтрайия по onlyAvailable
        return ((root, query, criteriaBuilder) -> {
            if (sort != null && sort.equals(SORT_EVENT_DATE)) {
                query.orderBy(criteriaBuilder.desc(root.get(EVENT_DATE_CONST)));
            }
            filter.getFilters().add(buildPublishedFilterParam());
            final var predicates = buildFinalPredicate(root, criteriaBuilder, filter);
            return predicates;
        });
    }

    private ClientFilterParam buildPublishedFilterParam() {
        return new ClientFilterParam().setProperty("state")
                .setOperator(Comparison.EQ).setMainValue(PUBLISHED_VALUE_CONST);
    }
}
