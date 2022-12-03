package ru.practicum.ewmcore.service.participationRequest;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewmcore.converter.ParticipationRequestDtoConverter;
import ru.practicum.ewmcore.model.participationRequest.ParticipationRequestDto;
import ru.practicum.ewmcore.repository.ParticipationRequestRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestInternalService {
    private final ParticipationRequestRepository requestRepository;
    private final ParticipationRequestDtoConverter requestConverter;

    @Override
    public Optional<ParticipationRequestDto> findRequestUserOnEvent(Long userId, Long eventId) {
        final var requests = requestRepository.findByRequesterIdAndEventId(userId, eventId);
        return requests.map(requestConverter::convertFromEntity);
    }

}
