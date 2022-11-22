package ru.practicum.ewmcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

}
