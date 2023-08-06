package school.faang.user_service.service.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.entity.recommendation.SkillRequest;
import school.faang.user_service.mapper.recommendation.RecommendationRequestMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;
import school.faang.user_service.repository.recommendation.SkillRequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationRequestService {
    private final RecommendationRequestRepository recommendationRequestRepository;
    private final SkillRequestRepository skillRequestRepository;
    private final UserRepository userRepository;
    private final RecommendationRequestMapper recommendationRequestMapper;
    public void create(RecommendationRequestDto dto) {
        validateRecommendationRequest(dto);
        RecommendationRequest recommendationRequest = recommendationRequestMapper.toEntity(dto);

        Long requestId = recommendationRequest.getRequester().getId();
        Long receiverId = recommendationRequest.getReceiver().getId();
        String message = recommendationRequest.getMessage();

        if (userRepository.existsById(requestId) && userRepository.existsById(receiverId)) {
            if (!hasPendingRequest(requestId, receiverId)){
                recommendationRequest.setCreatedAt(LocalDateTime.now());
                recommendationRequest.setUpdatedAt(LocalDateTime.now());
                recommendationRequest.setStatus(RequestStatus.PENDING);

                RecommendationRequest savedRequest = recommendationRequestRepository.
                        create(requestId, receiverId, message);

                for (SkillRequest skillRequest : recommendationRequest.getSkills()) {
                    skillRequest.setRequest(savedRequest);
                    skillRequestRepository.create(requestId,skillRequest.getId());
                }
            } else {
                throw new IllegalArgumentException("A recommendation request between the same users can only be sent once every six months!");
            }
        } else {
            throw new IllegalArgumentException("Requester or receiver does not exist!");
        }
    }

    private void validateRecommendationRequest(RecommendationRequestDto dto) {
        if (dto.getMessage() == null || dto.getMessage().isEmpty()){
            throw new IllegalArgumentException("Empty recommendation request!");
        }
    }

    private boolean hasPendingRequest(long requesterId, long receiverId) {
        Optional<RecommendationRequest> latestRequest = recommendationRequestRepository.
                findLatestPendingRequest(requesterId, receiverId);
        if (latestRequest.isPresent()) {
            LocalDateTime createdAt = latestRequest.get().getCreatedAt();
            LocalDateTime sixMonthAgo = LocalDateTime.now().minusMonths(6);
            return createdAt.isAfter(sixMonthAgo);
        }
        return false;
    }
}
