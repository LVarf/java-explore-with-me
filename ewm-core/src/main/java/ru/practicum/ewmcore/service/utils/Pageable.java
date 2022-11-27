package ru.practicum.ewmcore.service.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Deprecated
@Getter
public class Pageable extends PageRequest {

    public Pageable(@JsonProperty("pageNumber") int page, @JsonProperty("pageSize") int size,
                    @JsonProperty(value = "sort", required = false) SortEternity sort) {
        super(page, size, sort == null ? Sort.unsorted() : sort);
    }

    public static org.springframework.data.domain.Pageable ofNullable(Pageable pageable) {
        if (pageable == null) {
            return unpagedBase();
        }

        return pageable;
    }

    private static org.springframework.data.domain.Pageable unpagedBase() {
        return org.springframework.data.domain.Pageable.unpaged();
    }

    public static Pageable createFirst(int size) {
        return new Pageable(0, size, null);
    }

    public static Pageable of(int page, int size) {
        return new Pageable(page, size, null);
    }

}
