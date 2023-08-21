package school.faang.user_service.filter.recommendation;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.filter.RecommendationRequestFilterDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.filter.recommendation.RecommendationRequestFilter;

import java.util.stream.Stream;

@Component
public class RecommendationRequestStatusFilter implements RecommendationRequestFilter {

    @Override
    public boolean isApplicable(RecommendationRequestFilterDto recommendationRequestFilterDto) {
        return recommendationRequestFilterDto.getStatus() != null;
    }

    @Override
    public Stream<RecommendationRequest> apply(Stream<RecommendationRequest> recommendationRequests, RecommendationRequestFilterDto recommendationRequestFilterDto) {
        return recommendationRequests.filter(recommendationRequest -> recommendationRequest.getStatus().equals(recommendationRequestFilterDto.getStatus()));
    }
}