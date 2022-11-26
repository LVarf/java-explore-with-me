package ru.practicum.ewmcore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.service.utils.PageConverter;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.EventValidator;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicService {
    private final PageConverter<Event> pageConverter;
    private final EventShortDtoConverter shortConverter;
    private final EventValidator validator;

    public Page<EventShortDto> readAllPublic(ClientFilter filter, Timestamp rangeStart) {

        return enrichPageShort(null, null, null);
    }

    private Page<EventShortDto> enrichPageShort(Pageable pageable, Page<Event> repositoryPage, Timestamp rangeStart) {
        final var page = pageConverter.convertToPage(repositoryPage, pageable);
        final List<EventShortDto> pageContent = page.first.stream()
                .map(shortConverter::convertFromEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new PageImpl<>(pageContent, page.second, repositoryPage.getTotalElements());
    }
}
