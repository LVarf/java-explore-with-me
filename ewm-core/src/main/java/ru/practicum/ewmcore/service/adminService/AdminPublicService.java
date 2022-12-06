package ru.practicum.ewmcore.service.adminService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface AdminPublicService {
    Page<EventFullDto> readAllByFilters(ClientFilter filters, Pageable pageable);

    Optional<EventFullDto> updateEventById(Long eventId, EventFullDto event);

    Optional<EventFullDto> publishEvent(Long eventId);

    Optional<EventFullDto> rejectEvent(Long eventId);

    Optional<CategoryDto> updateCategory(CategoryDto category);

    Optional<CategoryDto> createCategory(CategoryDto categoryDto);

    String deleteCategory(Long catId);

    Page<UserFullDto> findAllUsers(ClientFilter filter, Pageable pageable);

    Optional<UserFullDto> createUser(UserFullDto userFullDto);
}
