package ru.practicum.ewmstat.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmstat.model.EndpointHit;
import ru.practicum.ewmstat.model.EndpointHitDto;

@Component
@RequiredArgsConstructor
public class EndpointHitDtoConverter implements RootModelConverter<EndpointHitDto, EndpointHit> {

    private final TimeUtils timeUtils;

    @Override
    public EndpointHitDto convertFromEntity(final EndpointHit entity) {
        final EndpointHitDto model = new EndpointHitDto();
        BeanUtils.copyProperties(entity, model);
        model.setTimestamp(timeUtils.timestampToString(entity.getTimestamp()));
        return model;
    }

    @Override
    public EndpointHit convertToEntity(final EndpointHitDto entity) {
        final EndpointHit model = new EndpointHit();
        BeanUtils.copyProperties(entity, model);
        model.setTimestamp(timeUtils.stringToTimestamp(entity.getTimestamp()));
        return model;
    }

    @Override
    public EndpointHit mergeToEntity(final EndpointHitDto model, final EndpointHit entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
