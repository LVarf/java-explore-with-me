package ru.practicum.ewmcore.model.event;

import lombok.Data;
import ru.practicum.ewmcore.model.category.Category;
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
@Table(name = "events", schema = "public")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @Column
    private Integer confirmedRequests;
    @Column
    private Timestamp createdOn; //дата создания для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @Column
    private String description;
    @Column
    private Timestamp eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;
    @Column
    private Float locationLat;
    @Column
    private Float locationLon;
    @Column
    private boolean paid;
    @Column
    private Integer participantLimit;
    @Column
    private Timestamp publishedOn; //дата публикации для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @Column
    private boolean requestModeration;
    @Column
    @Enumerated(EnumType.STRING)
    private EventStateEnum state;
    @Column
    private String title;
    @Column
    private Integer views;
}
