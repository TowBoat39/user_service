package school.faang.user_service.filters.goal;

import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.filters.goal.dto.GoalFilterDto;

import java.util.stream.Stream;

public interface GoalFilter {
    boolean isApplicable(GoalFilterDto goalFilterDto);

    Stream<Goal> apply(Stream<Goal> goals, GoalFilterDto goalFilterDto);
}