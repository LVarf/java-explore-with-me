package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentController {
    private static final String CREATE_DATE_CONST = "createDate";
    private final CommentInternalService commentService;

    @GetMapping("/{eventId}")
    public List<CommentDto> readAllComments(@PathVariable Long eventId,
                                            @PageableDefault(sort = {CREATE_DATE_CONST},
                                                    direction = Sort.Direction.ASC) Pageable pageable) {
        return commentService.readAllComments(eventId, pageable);
    }

    @GetMapping("/comment/{comId}")
    public Optional<CommentDto> readComment(@PathVariable Long comId) {
        return commentService.readComment(comId);
    }

    @PostMapping("/{eventId}/{userId}")
    public Optional<CommentDto> createComment(@PathVariable Long eventId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto comment) {
        return commentService.createComment(eventId, userId, comment);
    }

    @PatchMapping("/{comId}/{userId}")
    public Optional<CommentDto> updateComment(@PathVariable Long comId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto comment) {
        return commentService.updateComment(comId, userId, comment);
    }

    @DeleteMapping("/{comId}/{userId}")
    public String deleteComment(@PathVariable Long comId,
                                @PathVariable Long userId) {
        return commentService.deleteComment(comId, userId);
    }
}
