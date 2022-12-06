package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.records.EventToCompilation;

@Repository
public interface EventToCompilationRepository extends JpaRepository<EventToCompilation, Long> {
}
