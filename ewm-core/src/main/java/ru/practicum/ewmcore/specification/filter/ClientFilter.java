package ru.practicum.ewmcore.specification.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ClientFilter {
    private List<ClientFilterParam> filters;
}
