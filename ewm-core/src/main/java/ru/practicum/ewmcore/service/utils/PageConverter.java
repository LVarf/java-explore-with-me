package ru.practicum.ewmcore.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Deprecated
@Component
@Slf4j
public class PageConverter<T> {

    public Pair<List<T>, Pageable> convertToPage(Page<T> allTopLevel, Pageable pageable) {
        final Pageable pageableObj;
        pageableObj = Objects.requireNonNullElseGet(pageable, Pageable::unpaged);
        final List<T> pageConverterRaw = allTopLevel.getContent();
        log.debug("Read page size {}, total {}", pageConverterRaw.size(), allTopLevel.getTotalElements());
        return new Pair<>(pageConverterRaw, pageableObj);
    }
}
