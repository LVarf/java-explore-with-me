package ru.practicum.ewmcore.model.compilation;

import lombok.Data;
import lombok.ToString;
import ru.practicum.ewmcore.model.records.EventToCompilation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Table(name = "compilation", schema = "ewm_core")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private EventToCompilation eventToCompilations;
    @Column
    private Boolean pinned;
    @Column
    private String title;
}
