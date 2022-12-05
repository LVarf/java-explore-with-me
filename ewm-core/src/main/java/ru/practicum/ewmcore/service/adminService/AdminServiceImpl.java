package ru.practicum.ewmcore.service.adminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.service.categoryService.CategoryInternalService;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminPublicService {
    private final EventInternalService eventService;
    private final CategoryInternalService categoryService;

    @Override
    public Page<EventFullDto> readAllByFilters(ClientFilter filters, Pageable pageable) {
        eventService.readAllEventsByFilters(filters, pageable);
        return Page.empty();
    }

    @Override
    public Optional<EventFullDto> updateEventById(Long eventId, EventFullDto event) {
        return eventService.updateEventById(eventId, event);
    }

    @Override
    public Optional<EventFullDto> publishEvent(Long eventId) {
        return eventService.updateEventOnPublish(eventId);
    }

    @Override
    public Optional<EventFullDto> rejectEvent(Long eventId) {
        return eventService.updateEventToReject(eventId);
    }

    @Override
    public Optional<CategoryDto> updateCategory(CategoryDto category) {
        return categoryService.updateCategoryInternal(category);
    }

    @Override
    public Optional<CategoryDto> createCategory(CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }
}
