package ru.practicum.ewmcore.specification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientFilterParam {
    private Comparison operator;
    private String property;
    private Object mainValue;
    private Object alterValue;

    public static ClientFilterParam of(Comparison operator, String property, String... values) {
        return of(operator, property, values, 0);
    }

    public static ClientFilterParam of(Comparison operator, String property, String[] values, int offset) {
        final String value = values[offset];
        switch (operator) {
            case LT:
            case LE:
                return new ClientFilterParam(operator, property, value, value );
            case IN:
                final List<String> objects = Arrays.stream(values).skip(offset).collect(Collectors.toList());
                return new ClientFilterParam(operator, property, objects, null);
            default:
                return new ClientFilterParam(operator, property, value, null);
        }
    }
}
