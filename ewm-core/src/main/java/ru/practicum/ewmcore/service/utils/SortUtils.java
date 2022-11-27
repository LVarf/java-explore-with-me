package ru.practicum.ewmcore.service.utils;

import org.springframework.data.domain.Sort;

@Deprecated
public interface SortUtils {
    Sort addSecondarySorting(Sort sort, String... fieldNames);
}
