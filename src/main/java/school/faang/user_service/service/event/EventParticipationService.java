package school.faang.user_service.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.event.EventParticipationRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventParticipationService {
    private final EventParticipationRepository eventParticipationRepository;

    private void validateEventId(Long eventId) {
        if (!eventParticipationRepository.existsById(eventId)) {
            throw new IllegalArgumentException("There is not event with this ID!");
        }
    }

    public void registerParticipant(long eventId, long userId) {
        validateEventId(eventId);
        List<User> users = eventParticipationRepository.findAllParticipantsByEventId(eventId);
        for (User user : users) {
            if (user.getId() == userId) {
                throw new DataValidationException("You are registered already!");
            }
        }
        eventParticipationRepository.register(eventId, userId);
    }

    public void unregisterParticipant(long eventId, long userId) {
        validateEventId(eventId);
        List<User> users = eventParticipationRepository.findAllParticipantsByEventId(eventId);
        for (User user : users) {
            if (user.getId() != userId) {
                throw new DataValidationException("You are not registered yet!");
            }
        }
        eventParticipationRepository.unregister(eventId, userId);
    }

    public int getCountRegisteredParticipant(long eventId) {
        validateEventId(eventId);
        return eventParticipationRepository.countParticipants(eventId);
    }
}