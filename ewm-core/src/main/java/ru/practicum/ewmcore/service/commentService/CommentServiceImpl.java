package ru.practicum.ewmcore.service.commentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CommentDtoConverter;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.repository.CommentRepository;
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.repository.UserRepository;
import ru.practicum.ewmcore.specification.CommentSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;
import ru.practicum.ewmcore.validator.EventDtoValidator;
import ru.practicum.ewmcore.validator.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentInternalService {
    private static final String EVENT_CONST = "event";
    private static final String DELETE_DATE_CONST = "deleteDate";
    private final EventDtoValidator eventDtoValidator;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentSpecification commentSpecification;
    private final CommentDtoConverter converter;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final TimeUtils timeUtils;

    @Override
    public List<CommentDto> readAllComments(Long eventId, Pageable pageable) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventDtoValidator.validationOnExist(eventFromDb.orElse(null));
        final var specifications = commentSpecification.findAllSpecification(
                new ClientFilter(List.of(new ClientFilterParam().setOperator(Comparison.EQ)
                        .setProperty(EVENT_CONST).setMainValue(eventId),
                        new ClientFilterParam().setOperator(Comparison.IS_NULL)
                                .setProperty(DELETE_DATE_CONST))
                )
        );
        final var commentsFromDb = commentRepository.findAll(specifications, pageable);
        return commentsFromDb.map(converter::convertFromEntity).toList()/*.stream()
                .filter(commentDto -> commentDto.getDeleteDate() == null).collect(Collectors.toList())*/;
    }

    @Override
    public Optional<CommentDto> readComment(Long comId) {
        return Optional.empty();
    }

    @Override
    public Optional<CommentDto> createComment(Long eventId, Long userId, CommentDto comment) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventDtoValidator.validationOnExist(eventFromDb.orElse(null));
        final var userFromDb = userRepository.findUserById(userId);
        userValidator.assertValidator(userFromDb.orElse(null) == null, this.getClass().getName());
        final var commitForSave = converter.convertToEntity(comment);
        commitForSave.setCreateDate(timeUtils.now());
        commitForSave.setEvent(eventFromDb.orElseThrow());
        commitForSave.setEventOwner(eventFromDb.orElseThrow().getInitiator());
        commitForSave.setCommentOwner(userFromDb.orElseThrow());
        final var commitFromSave = commentRepository.save(commitForSave);
        return Optional.of(converter.convertFromEntity(commitFromSave));
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
