package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

@Transactional
public interface UserInternalService {

    @Transactional(readOnly = true)
    Page<UserFullDto> findAllUsersInternal(ClientFilter filter, Pageable pageable);

    @Transactional(readOnly = true)
    Optional<UserFullDto> findUserByIdInternal(Long ids);

    @Transactional(readOnly = true)
    Optional<User> findUserByIdInternalImpl(Long ids);

    Optional<UserFullDto> createUserInternal(UserFullDto userFullDto);

    String deleteUserInternal(Long userId);
}
