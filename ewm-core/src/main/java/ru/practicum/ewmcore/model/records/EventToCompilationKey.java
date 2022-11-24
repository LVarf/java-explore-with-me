package ru.practicum.ewmcore.model.records;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@Accessors(chain = true)
public class EventToCompilationKey implements Serializable {
    @Column(name = "event_id", nullable = false)
    private Long event;

    @Column(name = "compilation_id", nullable = false)
    private Long compilation;
}
