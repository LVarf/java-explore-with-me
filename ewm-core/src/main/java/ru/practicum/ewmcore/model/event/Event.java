package ru.practicum.ewmcore.model.event;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import ru.practicum.ewmcore.model.category.Category;
import ru.practicum.ewmcore.model.records.EventToCompilation;
import ru.practicum.ewmcore.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@Table(name = "events", schema = "ewm_core")
@Accessors(chain = true)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "category_id", referencedColumnName = "id")
    })
    @ToString.Exclude
    private Category category;
    @Column
    private Integer confirmedRequests; //вычисляемое поле
    @Column
    private Timestamp createdOn; //дата создания для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @Column
    private String description;
    @Column
    private Timestamp eventDate; //дата события для дто (в формате "yyyy-MM-dd HH:mm:ss")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "initiator_id", referencedColumnName = "id")
    })
    @ToString.Exclude
    private User initiator;
    @OneToMany(mappedBy = "event")
    @ToString.Exclude
    private Set<EventToCompilation> eventToCompilations;
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
}
