package ru.practicum.ewmcore.service.commentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.CommentDtoConverter;
import ru.practicum.ewmcore.converter.TimeUtils;
import ru.practicum.ewmcore.model.comment.Comment;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.repository.CommentRepository;
import ru.practicum.ewmcore.repository.EventRepository;
import ru.practicum.ewmcore.repository.UserRepository;
import ru.practicum.ewmcore.service.commentService.CommentInternalService;
import ru.practicum.ewmcore.service.commentService.CommentPublicService;
import ru.practicum.ewmcore.specification.CommentSpecification;
import ru.practicum.ewmcore.specification.filter.ClientFilter;
import ru.practicum.ewmcore.specification.filter.ClientFilterParam;
import ru.practicum.ewmcore.specification.filter.Comparison;
import ru.practicum.ewmcore.validator.CommentValidator;
import ru.practicum.ewmcore.validator.EventDtoValidator;
import ru.practicum.ewmcore.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentPublicService, CommentInternalService {
    private static final String EVENT_CONST = "event";
    private static final String DELETE_DATE_CONST = "deleteDate";
    private static final String DELETE_MESSAGE = "Комментарий удалён.";
    private static final String ALREADY_DELETE_MESSAGE = "Комментарий уже удалён.";
    private final EventDtoValidator eventDtoValidator;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentSpecification commentSpecification;
    private final CommentValidator validator;
    private final CommentDtoConverter converter;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final TimeUtils timeUtils;

    @Override
    public List<CommentDto> readAllCommentsPublic(Long eventId, Pageable pageable) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventDtoValidator.validationOnExist(eventFromDb.orElse(null));
        final var specifications = commentSpecification.findAllSpecification(
                new ClientFilter(List.of(new ClientFilterParam().setOperator(Comparison.EQ)
                                .setProperty(EVENT_CONST).setMainValue(eventId),
                        new ClientFilterParam().setOperator(Comparison.IS_NULL)
                                .setProperty(DELETE_DATE_CONST))
                )
        );
        return readAllByFiltersImpl(specifications, pageable).toList();
    }

    @Override
    public Page<CommentDto> readAllByFiltersInternal(ClientFilter filter, Pageable pageable) {
        final var specifications = commentSpecification.findAllSpecification(filter);
        return readAllByFiltersImpl(specifications, pageable);
    }

    private Page<CommentDto> readAllByFiltersImpl(Specification<Comment> specifications, Pageable pageable) {
        final var commentsFromDb = commentRepository.findAll(specifications, pageable);
        return commentsFromDb.map(converter::convertFromEntity);
    }

    @Override
    public Optional<CommentDto> readCommentPublic(Long comId) {
        return readCommentImpl(comId).map(converter::convertFromEntity);
    }

    private Optional<Comment> readCommentImpl(Long comId) {
        final var commentFromDb = commentRepository.findById(comId);
        validator.assertValidator(commentFromDb.isEmpty() ||
                        commentFromDb.orElseThrow().getDeleteDate() != null, this.getClass().getName(),
                "Комментарий не найден");
        return commentFromDb;
    }

    @Override
    public Optional<CommentDto> readCommentInternal(Long comId) {
        final var commentFromDb = commentRepository.findById(comId);
        validator.assertValidator(commentFromDb.isEmpty(), this.getClass().getName(), "Комментарий не найден");
        return commentFromDb.map(converter::convertFromEntity);
    }

    @Override
    public Optional<CommentDto> createCommentPublic(Long eventId, Long userId, CommentDto comment) {
        final var eventFromDb = eventRepository.findById(eventId);
        eventDtoValidator.validationOnExist(eventFromDb.orElse(null));
        final var userFromDb = userRepository.findUserById(userId);
        userValidator.assertValidator(userFromDb.orElse(null) == null, this.getClass().getName(),
                "df");
        validator.validationText(comment.getText());
        final var commitForSave = converter.convertToEntity(comment);
        commitForSave.setCreateDate(timeUtils.now());
        commitForSave.setEvent(eventFromDb.orElseThrow());
        commitForSave.setEventOwner(eventFromDb.orElseThrow().getInitiator());
        commitForSave.setCommentOwner(userFromDb.orElseThrow());
        final var commitFromSave = commentRepository.save(commitForSave);
        return Optional.of(converter.convertFromEntity(commitFromSave));
    }

    @Override
    public Optional<CommentDto> updateCommentPublic(Long comId, Long userId, CommentDto comment) {
        final var userFromDb = userRepository.findUserById(userId);
        final var commentFromDb = readCommentImpl(comId).orElseThrow();
        userValidator.assertValidator(userFromDb.isEmpty() ||
                        !commentFromDb.getCommentOwner().getId().equals(userId), this.getClass().getName(),
                "Ошибка запроса: пользователь не зарегистрирован.");
        userValidator.assertValidator(!commentFromDb.getCommentOwner().getId().equals(userId), this.getClass().getName(),
                "Ошибка запроса: сообщение может редактировать только его создатель.");
        validator.validationText(comment.getText());
        commentFromDb.setText(comment.getText());
        commentFromDb.setUpdateDate(timeUtils.now());
        final var commentFromSave = commentRepository.save(commentFromDb);
        return Optional.of(commentFromSave).map(converter::convertFromEntity);
    }

    @Override
    public String deleteCommentPublic(Long comId, Long userId) {
        final var userFromDb = userRepository.findUserById(userId);
        final var commentFromDb = readCommentImpl(comId).orElseThrow();
        userValidator.assertValidator(userFromDb.isEmpty(), this.getClass().getName(),
                "Ошибка запроса: пользователь не зарегистрирован.");
        userValidator.assertValidator(!commentFromDb.getCommentOwner().getId().equals(userId),
                this.getClass().getName(), "Ошибка запроса: сообщение может удалить только его создатель");
        return deleteCommentImpl(commentFromDb);
    }

    @Override
    public String deleteCommentInternal(Long comId) {
        final var commentFromDb = commentRepository.findById(comId).orElseThrow();
        return deleteCommentImpl(commentFromDb);
    }

    private String deleteCommentImpl(Comment commentFromDb) {
        if (commentFromDb.getDeleteDate() != null) {
            return ALREADY_DELETE_MESSAGE;
        }
        commentFromDb.setDeleteDate(timeUtils.now());
        commentRepository.save(commentFromDb);
        return DELETE_MESSAGE;
    }
}
