package ru.practicum.ewmcore.service.commentService;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.comment.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentPublicService {

    List<CommentDto> readAllCommentsPublic(Long eventId, Pageable pageable);

    Optional<CommentDto> readCommentPublic(Long comId);

    Optional<CommentDto> createCommentPublic(Long eventId, Long userId, CommentDto comment);

    Optional<CommentDto> updateCommentPublic(Long comId, Long userId, CommentDto comment);

    String deleteCommentPublic(Long comId, Long userId);
}
