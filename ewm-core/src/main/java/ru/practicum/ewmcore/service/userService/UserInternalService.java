package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.user.User;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface UserInternalService {

    Page<UserFullDto> findAllUsersInternal(ClientFilter filter, Pageable pageable);

    Optional<UserFullDto> findUserByIdInternal(Long ids);

    Optional<User> findUserByIdInternalImpl(Long ids);

    Optional<UserFullDto> createUserInternal(UserFullDto userFullDto);

    String deleteUserInternal(Long userId);
}
