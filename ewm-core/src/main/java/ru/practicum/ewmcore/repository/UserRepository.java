package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
