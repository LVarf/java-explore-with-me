package ru.practicum.ewmcore.model.event;

import lombok.Data;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Integer views;
}
