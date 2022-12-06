package ru.practicum.ewmcore.service.userService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewmcore.model.user.UserFullDto;
import ru.practicum.ewmcore.specification.filter.ClientFilter;

import java.util.Optional;

public interface UserInternalService {

    public Page<UserFullDto> findAllUsersInternal(ClientFilter filter, Pageable pageable);

    Optional<UserFullDto> createUserInternal(UserFullDto userFullDto);

    String deleteUserInternal(Long userId);
}
