package ru.practicum.ewmcore.model.commentReports;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.ewmcore.model.comment.Comment;
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
@Table(name = "comment_reports", schema = "ewm_core")
@Accessors(chain = true)
public class CommentReports {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Comment comment;
    @ManyToOne(fetch = FetchType.LAZY)
    private User reportOwner;
    @Column
    private Timestamp createDate;
    @Column
    private Boolean actual;
}
