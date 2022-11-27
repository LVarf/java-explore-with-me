package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.event.EventShortDto;

import java.util.Optional;

public interface UserPublicService {
    @Transactional(readOnly = true)
    Page<EventShortDto> readAllEventsPublic(Long userId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<EventShortDto> readEventPublic();

    @Transactional
    Optional<EventFullDto> updateEventPublic(Long id, EventFullDto event);
}
