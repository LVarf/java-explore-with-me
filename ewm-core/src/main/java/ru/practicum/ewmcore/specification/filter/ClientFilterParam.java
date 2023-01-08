package ru.practicum.ewmcore.specification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ClientFilterParam {
    private Comparison operator;
    private String property;
    private Object mainValue;
    private Object alterValue;
}
