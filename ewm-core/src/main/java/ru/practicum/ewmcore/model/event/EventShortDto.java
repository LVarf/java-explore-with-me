package ru.practicum.ewmcore.model.event;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.user.UserShortDto;

@Data
@Accessors(chain = true)
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String description;
    private String eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private UserShortDto initiator;
    private boolean paid;
    private String title;
    private Long views;
}
