package ru.practicum.ewmcore.service.utils;

/**
 *
 */
public interface RootModelConverter<M, E> {
    M convertFromEntity(E entity);

    E convertToEntity(M model);

    E mergeToEntity(M model, E originalEntity);
}
