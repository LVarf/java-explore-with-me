package ru.practicum.ewmcore.service.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Sort;

@Deprecated
public class OrderEternity extends Sort.Order {
    public OrderEternity(@JsonProperty(value = "direction") Sort.Direction direction,
                         @JsonProperty(value = "property") String property) {
        super(direction, property);
    }
}
