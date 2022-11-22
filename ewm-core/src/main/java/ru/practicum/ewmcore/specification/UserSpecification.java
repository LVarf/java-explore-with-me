package ru.practicum.ewmcore.specification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmcore.model.user.User;

@Component
@RequiredArgsConstructor
public class UserSpecification extends AbstractSpecification<User>{
}
