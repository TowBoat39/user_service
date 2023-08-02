package school.faang.user_service.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.recommendation.RecommendationDto;
import school.faang.user_service.dto.recommendation.RecommendationUpdateDto;
import school.faang.user_service.service.RecommendationService;
import school.faang.user_service.validator.RecommendationChecker;

@RestController
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private RecommendationChecker recommendationChecker;

    @PostMapping("/create")
    public ResponseEntity<RecommendationDto> giveRecommendation(@Valid @RequestBody RecommendationDto recommendation) {
        recommendationChecker.validate(recommendation);
        RecommendationDto newRecommendation = recommendationService.create(recommendation);
        return ResponseEntity.ok(newRecommendation);
    }

    @PutMapping("/update")
    public ResponseEntity<RecommendationDto> updateRecommendation(@Valid @RequestBody RecommendationUpdateDto toUpdate) {
        RecommendationDto newUpdatedRecommendation = recommendationService.update(toUpdate);
        return ResponseEntity.ok(newUpdatedRecommendation);
    }
}

