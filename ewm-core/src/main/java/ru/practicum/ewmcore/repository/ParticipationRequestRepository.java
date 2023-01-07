package ru.practicum.ewmcore.repository;

import com.sun.java.accessibility.util.EventID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequest;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestStateEnum;
import ru.practicum.ewmcore.model.user.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findByEventId(Long eventId);

    List<ParticipationRequest> findByStatus(ParticipationRequestStateEnum stateEnum);

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);
}
