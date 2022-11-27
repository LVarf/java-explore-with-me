package ru.practicum.ewmcore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.EventShortDtoConverter;
import ru.practicum.ewmcore.model.event.EventShortDto;
//import ru.practicum.ewmcore.service.utils.PageConverter;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.validator.EventDtoValidator;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class PublicService {
    //private final PageConverter<Event> pageConverter;
    private final EventShortDtoConverter shortConverter;
    private final EventDtoValidator validator;

    public Page<EventShortDto> readAllPublic(ClientFilter filter, Timestamp rangeStart) {

        return null;
    }

    /*private Page<EventShortDto> enrichPageShort(Pageable pageable, Page<Event> repositoryPage, Timestamp rangeStart) {
        final var page = pageConverter.convertToPage(repositoryPage, pageable);
        final List<EventShortDto> pageContent = page.first.stream()
                .map(shortConverter::convertFromEntity)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return new PageImpl<>(pageContent, page.second, repositoryPage.getTotalElements());
    }*/
}
