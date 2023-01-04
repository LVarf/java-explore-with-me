package ru.practicum.ewmcore.model.records;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class EventToCompilationKey implements Serializable {
    @Column(name = "event_id", nullable = false)
    private Long event;

    @Column(name = "compilation_id", nullable = false)
    private Long compilation;
}
