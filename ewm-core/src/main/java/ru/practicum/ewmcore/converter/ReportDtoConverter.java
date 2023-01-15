package ru.practicum.ewmcore.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.commentReports.Report;
import ru.practicum.ewmcore.model.commentReports.ReportDto;
import ru.practicum.ewmcore.service.utils.RootModelConverter;

@Component
@RequiredArgsConstructor
public class ReportDtoConverter
        implements RootModelConverter<ReportDto, Report> {
    private final TimeUtils timeUtils;

    @Override
    public ReportDto convertFromEntity(final Report entity) {
        final ReportDto model = new ReportDto();
        BeanUtils.copyProperties(entity, model);
        return model;
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
