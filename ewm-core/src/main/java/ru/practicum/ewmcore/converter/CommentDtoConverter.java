package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.comment.Comment;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class CommentDtoConverter
        implements RootModelConverter<CommentDto, Comment> {
    private final TimeUtils timeUtils;
    private final UserShortDtoConverter userShortDtoConverter;

    @Override
    public CommentDto convertFromEntity(final Comment entity) {
        final CommentDto model = new CommentDto();
        BeanUtils.copyProperties(entity, model);
        model.setCreateDate(timeUtils.timestampToString(entity.getCreateDate()));
        if (entity.getUpdateDate() != null) {
            model.setUpdateDate(timeUtils.timestampToString(entity.getUpdateDate()));
        }
        if (entity.getDeleteDate() != null) {
            model.setDeleteDate(timeUtils.timestampToString(entity.getDeleteDate()));
        }
        model.setCommentOwner(userShortDtoConverter.convertFromEntity(entity.getCommentOwner()));
        return model;
    }

    @Override
    public Comment convertToEntity(final CommentDto entity) {
        final Comment model = new Comment();
        BeanUtils.copyProperties(entity, model);
        model.setCreateDate(null);
        model.setUpdateDate(null);
        model.setDeleteDate(null);
        return model;
    }

    @Override
    public Comment mergeToEntity(final CommentDto model, final Comment entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
