package ru.practicum.ewmcore.service.commentService;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.comment.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentPublicService {
    @Transactional(readOnly = true)
    List<CommentDto> readAllCommentsPublic(Long eventId, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<CommentDto> readCommentPublic(Long comId);

    @Transactional
    Optional<CommentDto> createCommentPublic(Long eventId, Long userId, CommentDto comment);

    @Transactional
    Optional<CommentDto> updateCommentPublic(Long comId, Long userId, CommentDto comment);

    @Transactional
    String deleteCommentPublic(Long comId, Long userId);
}
