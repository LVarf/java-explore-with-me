package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.records.EventToCompilation;

import java.util.Optional;

@Repository
public interface EventToCompilationRepository extends JpaRepository<EventToCompilation, Long> {

    Optional<EventToCompilation> findByEventIdAndCompilationId(Long eventId, Long compId);
}
