package ru.practicum.ewmcore.model.commentReports;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportEntityEnum {
    EVENT(1),
    COMMENT(2),
    USER(3);

    private final int id;
}
