package ru.practicum.ewmcore.model.comment;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "comments", schema = "ewm_core")
@Accessors(chain = true)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    private User eventOwner;
    @ManyToOne(fetch = FetchType.LAZY)
    private User commentOwner;
    @Column
    private Timestamp createDate;
    @Column
    private Timestamp updateDate;
    @Column
    private Timestamp deleteDate;
}
