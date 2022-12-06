package ru.practicum.ewmcore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.event.Event;
import ru.practicum.ewmcore.model.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
