package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.compilation.CompilationDtoResponse;
import ru.practicum.ewmcore.model.event.EventShortDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationDtoResponseConverter implements RootModelConverter<CompilationDtoResponse, CompilationDto> {

    @Override
    public CompilationDtoResponse convertFromEntity(CompilationDto entity) {
        final CompilationDtoResponse model = new CompilationDtoResponse();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public CompilationDto convertToEntity(CompilationDtoResponse entity) {
        final CompilationDto model = new CompilationDto();
        BeanUtils.copyProperties(entity, model);
        final var events = entity.getEvents().stream()
                .map(el -> new EventShortDto().setId(el)).collect(Collectors.toSet());
        model.setEvents(events);
        return model;
    }

    @Override
    public CompilationDto mergeToEntity(CompilationDtoResponse model, CompilationDto originalEntity) {
        BeanUtils.copyProperties(model, originalEntity);
        return originalEntity;
    }
}
