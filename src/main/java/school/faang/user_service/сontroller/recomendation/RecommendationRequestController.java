package school.faang.user_service.сontroller.recomendation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.service.RecommendationRequestService;

@Controller
@RequiredArgsConstructor
public class RecommendationRequestController {
    private final RecommendationRequestService recommendationRequestService;

    public RecommendationRequestDto requestRecommendation(RecommendationRequestDto recommendationRequest) {
        if (!recommendationRequest.getMessage().isEmpty() && recommendationRequest.getMessage() != null) {
            recommendationRequestService.create(recommendationRequest);
            return recommendationRequest;
        } else {
            throw new IllegalArgumentException("Recommendation request message should not be empty");
        }
    }
}
