package ru.practicum.ewmcore.model.comment;

import lombok.Data;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
public class CommentDto {
    private Long id;
    private String text;
    private UserShortDto commentOwner;
}
