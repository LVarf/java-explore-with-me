package ru.practicum.ewmcore.model.commentReports;

import lombok.Data;
import ru.practicum.ewmcore.model.user.UserShortDto;

import java.sql.Timestamp;

@Data
public class ReportDto {
    private Long id;
    private String text;
    private Object entity;
    private UserShortDto reportOwner;
    private String createDate;
    private Timestamp updateDate;
    private Boolean actual;
}
