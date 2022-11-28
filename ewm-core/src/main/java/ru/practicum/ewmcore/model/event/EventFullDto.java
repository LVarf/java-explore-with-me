package ru.practicum.ewmcore.model.event;

import lombok.Data;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.location.Location;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
public class EventFullDto {
    private Long id;
    private String annotation;
    private Long category;
    private Integer confirmedRequests;
    private String createdOn; //дата создания для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private String description;
    private String eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private Integer participantLimit;
    private String publishedOn; //дата публикации для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private boolean requestModeration;
    private EventStateEnum state;
    private String title;
    private Integer views;
}
