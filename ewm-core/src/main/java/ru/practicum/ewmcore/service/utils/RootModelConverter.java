package ru.practicum.ewmcore.service.utils;

import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 */
public interface RootModelConverter<M, E> {
    M convertFromEntity(E entity);

    default List<M> convertFromEntity(final Iterable<E> entities) {
        return StreamSupport.stream(entities.spliterator(), false).
                map(this::convertFromEntity).collect(Collectors.toList());
    }

    E convertToEntity(M model);

    default Stream<E> convertToEntity(final Iterable<M> entities) {
        return StreamSupport.stream(entities.spliterator(), false).
                map(this::convertToEntity);
    }

    E mergeToEntity(M model, E originalEntity);

    default Sort convertForPersistence(final Sort sort) {
        return sort;
    }
}
