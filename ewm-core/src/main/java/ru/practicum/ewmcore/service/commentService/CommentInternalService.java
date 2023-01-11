package ru.practicum.ewmcore.service.commentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.comment.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentInternalService {
    @Transactional(readOnly = true)
    List<CommentDto> readAllComments(Long eventId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CommentDto> readComment(Long comId);

    @Transactional
    Optional<CommentDto> createComment(Long eventId, Long userId, CommentDto comment);

    @Transactional
    Optional<CommentDto> updateComment(Long comId, Long userId, CommentDto comment);

    @Transactional
    String deleteComment(Long comId, Long userId);
}
