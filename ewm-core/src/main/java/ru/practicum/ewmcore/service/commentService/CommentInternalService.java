package ru.practicum.ewmcore.service.commentService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface CommentInternalService {

    @Transactional(readOnly = true)
    Optional<CommentDto> readCommentInternal(Long comId);

    @Transactional(readOnly = true)
    Page<CommentDto> readAllByFiltersInternal(ClientFilter filter, Pageable pageable);

    @Transactional
    String deleteCommentInternal(Long comId);
}
