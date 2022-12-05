package ru.practicum.ewmcore.service.adminService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

public interface AdminPublicService {
    Page<EventFullDto> readAllByFilters(ClientFilter filters, Pageable pageable);
}
