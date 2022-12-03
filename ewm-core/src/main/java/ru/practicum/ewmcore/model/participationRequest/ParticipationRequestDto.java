package ru.practicum.ewmcore.model.participationRequest;

import lombok.Data;

@Data
public class ParticipationRequestDto {
    private Long id;
    private String created; //дата создания для дто (в формате "yyyy-MM-dd HH:mm:ss")
    private Long event;
    private Integer requester;
    private ParticipationRequestStateEnum status;
}
