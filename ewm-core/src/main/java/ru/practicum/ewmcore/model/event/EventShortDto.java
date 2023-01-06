package ru.practicum.ewmcore.model.event;

import lombok.Data;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private Long category;
    private Integer confirmedRequests;
    private String description;
    private String eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
}
