package ru.practicum.ewmcore.model.commentReports;

import lombok.Data;
import lombok.experimental.Accessors;
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
@Table(name = "reports", schema = "ewm_core")
@Accessors(chain = true)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @Column
    @Enumerated(EnumType.STRING)
    private ReportEntityEnum entity;
    @Column
    private Long entityId;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportOwner;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportGoalUser;
    @Column
    private Timestamp createDate;
    @Column
    private Timestamp updateDate;
    @Column
    private Boolean actual;
    @Column
    private String commentAdmin;
}
