package ru.practicum.ewmcore.service.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public interface SortConverterMixin {

    default Pageable convertSort(Pageable pageable) {
        return convertSort(pageable, null);
    }

    default Pageable convertSort(Pageable pageable, String baseField) {
        if (pageable != null) {
            Sort sort = pageable.getSort();
            if (baseField != null) {
                sort = secondarySort(pageable, baseField).getSort();
            }
            if (!sort.isUnsorted()) {
                final List<Sort.Order> newOrders = sort.stream().map(this::convertSortPropertyToEntityStyle)
                        .collect(Collectors.toList());
                final Sort newSort = Sort.by(newOrders);
                return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
            } else {
                return pageable;
            }
        }

        return pageable;
    }

    Sort.Order convertSortPropertyToEntityStyle(Sort.Order order);

    Pageable secondarySort(Pageable pageable, String fieldName);
}