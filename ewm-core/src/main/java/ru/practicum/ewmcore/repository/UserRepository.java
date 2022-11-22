package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmcore.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
