package ru.practicum.ewmcore.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.model.comment.CommentDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentInternalService {
    @Override
    public Page<CommentDto> readAllComments(Long eventId) {
        return null;
    }

    @Override
    public Optional<CommentDto> readComment(Long comId) {
        return Optional.empty();
    }

    @Override
    public Optional<CommentDto> createComment(Long eventId, Long userId, CommentDto comment) {
        return Optional.empty();
    }

    @Override
    public Optional<CommentDto> updateComment(Long comId, Long userId, CommentDto comment) {
        return Optional.empty();
    }

    @Override
    public String deleteComment(Long comId, Long userId) {
        return null;
    }
}
