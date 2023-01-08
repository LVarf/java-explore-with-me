package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class ParticipationRequestDtoConverter
        implements RootModelConverter<ParticipationRequestDto, ParticipationRequest> {
    private final TimeUtils timeUtils;

    @Override
    public ParticipationRequestDto convertFromEntity(final ParticipationRequest entity) {
        final ParticipationRequestDto model = new ParticipationRequestDto();
        BeanUtils.copyProperties(entity, model);
        final var created = entity.getCreated();
        model.setCreated(timeUtils.timestampToString(created));
        model.setEvent(entity.getEvent().getId());
        model.setRequester(entity.getRequester().getId());
        return model;
    }

    @Override
    public ParticipationRequest convertToEntity(final ParticipationRequestDto entity) {
        final ParticipationRequest model = new ParticipationRequest();
        BeanUtils.copyProperties(entity, model);
        final var created = entity.getCreated();
        model.setCreated(timeUtils.stringToTimestamp(created));
        return model;
    }

    @Override
    public ParticipationRequest mergeToEntity(final ParticipationRequestDto model, final ParticipationRequest entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
