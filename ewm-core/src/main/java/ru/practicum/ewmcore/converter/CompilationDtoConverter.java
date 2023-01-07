package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.service.utils.RootModelConverter;
import ru.practicum.ewmcore.service.utils.SortConverterMixin;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationDtoConverter implements RootModelConverter<CompilationDto, Compilation>,
        SortConverterMixin {

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

    @Override
    public Sort.Order convertSortPropertyToEntityStyle(Sort.Order order) {
        return null;
    }

    @Override
    public Pageable secondarySort(Pageable pageable, String fieldName) {
        return null;
    }
}
