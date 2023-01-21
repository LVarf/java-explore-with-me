package ru.practicum.ewmcore.service.commentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface CommentInternalService {

    Optional<CommentDto> readCommentInternal(Long comId);

    Page<CommentDto> readAllByFiltersInternal(ClientFilter filter, Pageable pageable);

    String deleteCommentInternal(Long comId);
}
