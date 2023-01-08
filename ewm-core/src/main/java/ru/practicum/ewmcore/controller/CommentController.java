package ru.practicum.ewmcore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/comments")
public class CommentController {
    @GetMapping("/{eventId}")
    public Page<CommentDto> readAllComments(@PathVariable Long eventId,
                                            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC,
                                                    page = 0, size = 10) Pageable pageable) {
        //сортировка по дате создания
        return null;
    }

    @GetMapping("/{comId}")
    public Optional<CommentDto> readComment(@PathVariable Long comId) {
        return null;
    }

    @PostMapping("/{eventId}/{userId}")
    public Optional<CommentDto> createComment(@PathVariable Long eventId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto comment) {
        return null;
    }

    @PatchMapping("/{comId}/{userId}")
    public Optional<CommentDto> updateComment(@PathVariable Long comId,
                                              @PathVariable Long userId,
                                              @RequestBody CommentDto event) {
        return null;
    }

    @DeleteMapping("/{comId}/{userId}")
    public String updateEventOnCancel(@PathVariable Long comId,
                                      @PathVariable Long userId) {
        return null;
    }
}
