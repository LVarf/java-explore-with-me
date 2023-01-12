package ru.practicum.ewmcore.model.commentReports;

import lombok.Data;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
public class CommentReportsDto {
    private Long id;
    private String text;
    private CommentDto comment;
    private UserShortDto reportOwner;
    private String createDate;
}
