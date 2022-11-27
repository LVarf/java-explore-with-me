package ru.practicum.ewmcore.service.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class SortEternity extends Sort {
    protected SortEternity(@JsonProperty(value = "orders") List<OrderEternity> orders) {
        super(orders.stream().map(o -> (Order) o).collect(Collectors.toList()));
    }
}
