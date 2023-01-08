package ru.practicum.ewmcore.service.comment;

import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.comment.CommentDto;

import java.util.Optional;

public interface CommentInternalService {
    @Transactional(readOnly = true)
    Page<CommentDto> readAllComments(Long eventId);

    @Transactional(readOnly = true)
    Optional<CommentDto> readComment(Long comId);

    @Transactional
    Optional<CommentDto> createComment(Long eventId, Long userId, CommentDto comment);

    @Transactional
    Optional<CommentDto> updateComment(Long comId, Long userId, CommentDto comment);

    @Transactional
    String deleteComment(Long comId, Long userId);
}
