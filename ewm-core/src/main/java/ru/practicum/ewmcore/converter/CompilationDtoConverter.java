package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class CompilationDtoConverter implements RootModelConverter<CompilationDto, Compilation> {

    @Override
    public CompilationDto convertFromEntity(Compilation entity) {
        final CompilationDto model = new CompilationDto();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public Compilation convertToEntity(CompilationDto entity) {
        final Compilation model = new Compilation();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public Compilation mergeToEntity(CompilationDto model, Compilation originalEntity) {
        BeanUtils.copyProperties(model, originalEntity);
        return originalEntity;
    }
}
