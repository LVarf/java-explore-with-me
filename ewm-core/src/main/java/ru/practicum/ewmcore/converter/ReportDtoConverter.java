package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.commentReports.Report;
import ru.practicum.ewmcore.model.commentReports.ReportDto;
import ru.practicum.ewmcore.model.commentReports.ReportEntityEnum;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;
import ru.practicum.ewmcore.service.eventService.EventInternalService;
import ru.practicum.ewmcore.service.userService.UserInternalService;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class ReportDtoConverter implements RootModelConverter<ReportDto, Report> {
    private final TimeUtils timeUtils;
    private final CommentInternalService commentInternalService;
    private final EventInternalService eventInternalService;
    private final UserInternalService userInternalService;

    @Override
    public ReportDto convertFromEntity(final Report entity) {
        final ReportDto model = new ReportDto();
        BeanUtils.copyProperties(entity, model);
        model.setCreateDate(entity.getCreateDate() != null ?
                timeUtils.timestampToString(entity.getCreateDate()) : null);
        model.setUpdateDate(entity.getUpdateDate() != null ?
                timeUtils.timestampToString(entity.getUpdateDate()) : null);
        model.setEntity(enrichObjectToReport(entity));
        model.setObjectType(entity.getEntity());
        return model;
    }

    private Object enrichObjectToReport(Report entity) {
        if(entity.getEntity() == ReportEntityEnum.USER) {
            return userInternalService.findUserByIdInternal(entity.getEntityId());
        }
        if(entity.getEntity() == ReportEntityEnum.EVENT) {
            return eventInternalService.readEvent(entity.getEntityId());
        }
        if(entity.getEntity() == ReportEntityEnum.COMMENT) {
            return commentInternalService.readCommentInternal(entity.getEntityId());
        }
        return null;
    }

    @Override
    public Report convertToEntity(final ReportDto entity) {
        final Report model = new Report();
        BeanUtils.copyProperties(entity, model);
        model.setCreateDate(null);
        model.setUpdateDate(null);
        return model;
    }

    @Override
    public Report mergeToEntity(final ReportDto model, final Report entity) {
        BeanUtils.copyProperties(model, entity);
        return entity;
    }
}
