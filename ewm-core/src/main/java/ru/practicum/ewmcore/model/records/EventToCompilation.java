package ru.practicum.ewmcore.model.records;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.practicum.ewmcore.model.compilation.Compilation;
import ru.practicum.ewmcore.model.event.Event;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@RequiredArgsConstructor
@Entity
@Table(name = "event_to_compilation", schema = "ewm_core")
public class EventToCompilation {

    @EmbeddedId
    private EventToCompilationKey key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @ToString.Exclude
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id", referencedColumnName = "id",
            insertable = false, updatable = false)
    @ToString.Exclude
    private Compilation compilation;
}
