package ru.practicum.ewmcore.specification;

import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

public abstract class AbstractSpecification<T> {
    private static final String ID = "id";
    private static final String USER_ID = "userId";
    private static final String STATE = "state";
    private static final String CATEGORY = "category";
    private static final String RANGE_START = "rangeStart";
    private static final String RANGE_END = "rangeEnd";

    private TimeUtils timeUtils;

    protected Predicate buildFinalPredicate(final Root<T> root,
                                            final CriteriaBuilder criteriaBuilder,
                                            final ClientFilter filters) {
        final List<Predicate> predicates = new ArrayList<>(commonFilterPredicate(filters, root, criteriaBuilder));
        final List<Predicate> finalListPredicate = predicates.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return criteriaBuilder.and(finalListPredicate.toArray(new Predicate[0]));
    }

    private List<Predicate> commonFilterPredicate(final ClientFilter clientFilter, final Root<T> root,
                                                  final CriteriaBuilder criteriaBuilder) {
        if (clientFilter.getFilters().size() == 0) {
            return new ArrayList<>();
        } else {
            return clientFilter.getFilters().stream()
                    .map(clientFilterParam ->
                            convertFilterParamToPredicate(clientFilterParam, root, criteriaBuilder))
                    .collect(Collectors.toList());
        }
    }

    private Predicate convertFilterParamToPredicate(final ClientFilterParam clientFilterParam, final Root<T> root,
                                                    final CriteriaBuilder criteriaBuilder) {
        switch (clientFilterParam.getProperty()) {
            case ID:
            case USER_ID:
            case STATE:
            case CATEGORY:
                return buildPredicate(clientFilterParam, root, criteriaBuilder);
            case RANGE_START:
            case RANGE_END:
                return createTimestampPredicate(clientFilterParam, root, criteriaBuilder);
            default:
                return null;
        }
    }

    private Predicate createTimestampPredicate(final ClientFilterParam clientFilterParam, final Root<T> root,
                                               final CriteriaBuilder criteriaBuilder) {
        final Timestamp rangeStart = timeUtils.stringToTimestamp((String) clientFilterParam.getMainValue());
        final Timestamp rangeEnd = timeUtils.stringToTimestamp((String) clientFilterParam.getMainValue());
        switch (clientFilterParam.getOperator()) {
            case GE:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(clientFilterParam.getProperty()), rangeStart);
            case LE:
                return criteriaBuilder.lessThanOrEqualTo(root.get(clientFilterParam.getProperty()), rangeEnd);
            default:
                return null;
        }
    }

    protected Predicate buildPredicate(final ClientFilterParam clientFilterParam, final Root<T> root,
                                       final CriteriaBuilder criteriaBuilder) {
        final Comparison comparison = clientFilterParam.getOperator();
        final Object mainValue = clientFilterParam.getMainValue();
        final Object alterValue = clientFilterParam.getAlterValue();
        switch (comparison) {
            case LIKE_IGNORE_CASE:
                return criteriaBuilder
                        .like(criteriaBuilder.upper(root.get(clientFilterParam.getProperty()).as(String.class)),
                                mainValue.toString().toUpperCase());
            case LIKE:
                return criteriaBuilder.like(root.get(clientFilterParam.getProperty()), mainValue.toString());
            case NOT_LIKE_IGNORE_CASE:
                return criteriaBuilder.notLike(criteriaBuilder
                                .upper(root.get(clientFilterParam.getProperty()).as(String.class)),
                        mainValue.toString().toUpperCase());
            case NOT_LIKE:
                return criteriaBuilder
                        .notLike(root.get(clientFilterParam.getProperty()), mainValue.toString());
            case NE:
                return criteriaBuilder.notEqual(root.get(clientFilterParam.getProperty()), mainValue.toString());
            case BETWEEN:
                return criteriaBuilder
                        .between(root.get(clientFilterParam.getProperty()),
                                mainValue.toString(), alterValue.toString());
            case GE:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(clientFilterParam.getProperty()),
                        mainValue.toString());
            case LE:
                return criteriaBuilder.lessThanOrEqualTo(root.get(clientFilterParam.getProperty()),
                        mainValue.toString());
            case EQ:
                return criteriaBuilder.equal(root.get(clientFilterParam.getProperty()),
                        mainValue.toString());
            default:
                return null;
        }
    }
}
