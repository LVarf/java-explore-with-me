package ru.practicum.ewmcore.model.commentReports;

import lombok.Data;
import ru.practicum.ewmcore.model.user.UserShortDto;

import java.sql.Timestamp;

@Data
public class ReportDto {
    private Long id;
    private String text;
    private ReportEntityEnum objectType;
    private Object entity;
    private UserShortDto reportOwner;
    private UserShortDto reportGoalUser;
    private String createDate;
    private String updateDate;
    private Boolean actual;
    private String commentAdmin;
}
