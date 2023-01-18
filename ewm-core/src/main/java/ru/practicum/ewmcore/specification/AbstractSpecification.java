package ru.practicum.ewmcore.specification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractSpecification<T> {
    private static final String ID = "id";
    private static final String INITIATOR = "initiator";
    private static final String STATE = "state";
    private static final String CATEGORY = "category";
    private static final String RANGE_START = "rangeStart";
    private static final String RANGE_END = "rangeEnd";
    private static final String PAID = "paid";
    private static final String CONFIRMED_REQUESTS = "confirmedRequests";
    private static final String EVENT_DATE = "eventDate";
    private static final String EVENT = "event";
    private static final String DELETE_DATE = "deleteDate";
    private static final String COMMENT_OWNER = "commentOwner";

    @Autowired
    private TimeUtils timeUtils;

    protected Predicate buildFinalPredicate(final Root<T> root,
                                            final CriteriaBuilder criteriaBuilder,
                                            final ClientFilter filters) {
        final List<Predicate> predicates = new ArrayList<>(commonFilterPredicate(filters, root, criteriaBuilder));
        return criteriaBuilder.and(predicates.stream()
                .filter(Objects::nonNull).toArray(Predicate[]::new));
    }

    private List<Predicate> commonFilterPredicate(final ClientFilter clientFilter, final Root<T> root,
                                                  final CriteriaBuilder criteriaBuilder) {
        if (clientFilter.getFilters().isEmpty()) {
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
            case EVENT:
            case STATE:
            case CATEGORY:
            case INITIATOR:
            case CONFIRMED_REQUESTS:
            case DELETE_DATE:
            case PAID:
            case COMMENT_OWNER:
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
        final var mainValue = (String) clientFilterParam.getMainValue();
        final Timestamp rangeStart = timeUtils.stringToTimestamp(mainValue);
        final Timestamp rangeEnd = timeUtils.stringToTimestamp((String) clientFilterParam.getMainValue());
        switch (clientFilterParam.getOperator()) {
            case GE:
                return criteriaBuilder.greaterThanOrEqualTo(root.get(EVENT_DATE), rangeStart);
            case LE:
                return criteriaBuilder.lessThanOrEqualTo(root.get(EVENT_DATE), rangeEnd);
            default:
                return null;
        }
    }

    protected Predicate buildPredicate(final ClientFilterParam clientFilterParam, final Root<T> root,
                                       final CriteriaBuilder criteriaBuilder) {
        final Comparison comparison = clientFilterParam.getOperator();
        final Object mainValue = clientFilterParam.getMainValue();
        final Object alterValue = clientFilterParam.getAlterValue();
        log.info("Property: {}; Operator: {}; MainValue: {};", clientFilterParam.getProperty(),
                clientFilterParam.getOperator(), clientFilterParam.getMainValue());
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
            case LT:
                final var pred1 = criteriaBuilder.lessThan(root.get(clientFilterParam.getProperty()),
                        root.get(mainValue.toString()));
                final var pred2 = criteriaBuilder.greaterThan(root.get(mainValue.toString()), 0);
                return criteriaBuilder.and(pred1, pred2);
            case LE:
                return criteriaBuilder.lessThanOrEqualTo(root.get(clientFilterParam.getProperty()),
                        mainValue.toString());
            case IS_NULL:
                return criteriaBuilder.isNull(root.get(clientFilterParam.getProperty()));
            case EQ:
                final var getRoot = root.get(clientFilterParam.getProperty());
                return criteriaBuilder.equal(getRoot,
                        mainValue);
            default:
                return null;
        }
    }
}
