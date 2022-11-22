package ru.practicum.ewmcore.model.participationRequest;

import lombok.Data;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.event.EventStateEnum;
import ru.practicum.ewmcore.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "participation_request", schema = "public")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Timestamp created; //дата создания для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private User requester;
    @Column
    @Enumerated(EnumType.STRING)
    private ParticipationRequestStateEnum status;
}
