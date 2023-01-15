package ru.practicum.ewmcore.service.adminService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.category.CategoryDto;
import ru.practicum.ewmcore.model.comment.CommentDto;
import ru.practicum.ewmcore.model.commentReports.ReportDto;
import ru.practicum.ewmcore.model.compilation.CompilationDto;
import ru.practicum.ewmcore.model.event.EventFullDto;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface AdminPublicService {

    @Transactional(readOnly = true)
    Page<ReportDto> readAllReports(ClientFilter filter, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<ReportDto> readReport(Long reportId);

    @Transactional
    Optional<ReportDto> updateReport(Long reportId, ReportDto reportDto);

    Page<EventFullDto> readAllByFilters(ClientFilter filters, Pageable pageable);

    Optional<EventFullDto> updateEventById(Long eventId, EventFullDto event);

    Optional<EventFullDto> publishEvent(Long eventId);

    Optional<EventFullDto> rejectEvent(Long eventId);

    Optional<CategoryDto> updateCategory(CategoryDto category);

    Optional<CategoryDto> createCategory(CategoryDto categoryDto);

    String deleteCategory(Long catId);

    Page<UserFullDto> findAllUsers(ClientFilter filter, Pageable pageable);

    Optional<UserFullDto> findUserById(Long ids);

    Optional<UserFullDto> createUser(UserFullDto userFullDto);

    String deleteUser(Long userId);

    Optional<CompilationDto> createCompilation(CompilationDto compilationDto);

    String deleteCompilation(Long compId);

    String deleteEventFromCompilation(Long eventId, Long compId);

    String updateAddEventToCompilation(Long eventId, Long compId);

    String deleteCompilationFromHeadPage(Long compId);

    String addCompilationToHeadPage(Long compId);

    Page<CommentDto> readAllCommentsPublic(ClientFilter filter, Pageable pageable);

    Optional<CommentDto> readCommentPublic(Long comId);

    String deleteCommentPublic(Long comId);
}
