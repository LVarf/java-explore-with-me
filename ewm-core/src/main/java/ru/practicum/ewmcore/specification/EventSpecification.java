package ru.practicum.ewmcore.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

@Component
@RequiredArgsConstructor
public class EventSpecification extends AbstractSpecification<Event> {

    public Specification<Event> findAllSpecification(final ClientFilter filters) {
        return ((root, query, criteriaBuilder) -> {
            return buildFinalPredicate(root, criteriaBuilder, filters);
        });
    }
}
