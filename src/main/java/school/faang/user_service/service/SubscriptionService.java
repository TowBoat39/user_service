package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.commonMessages.ErrorMessages;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.filters.UserFilterDto;
import school.faang.user_service.exceptions.DataValidationException;
import school.faang.user_service.filters.UserMapper;
import school.faang.user_service.filters.filtersForUserFilterDto.DtoUserFilter;
import school.faang.user_service.repository.SubscriptionRepository;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final List<DtoUserFilter> userFilters;
    private final UserMapper userMapper;

    public void followUser(long followerId, long followeeId){
        validateFollower(followerId, followeeId);
        subscriptionRepository.followUser(followerId, followeeId);
    }

    public void unfollowUser(long followerId, long followeeId){
        validateFollower(followerId, followeeId);
        subscriptionRepository.unfollowUser(followerId, followeeId);
    }

    public List<UserDto> getFollowers(long followeeId, UserFilterDto filter){
        validateUserId(followeeId);
        return applyFilter(subscriptionRepository.findByFolloweeId(followeeId), filter);
    }

    public int getFollowersCount(long followeeId){
        validateUserId(followeeId);
        return subscriptionRepository.findFollowersAmountByFolloweeId(followeeId);
    }

    public List<UserDto> getFollowing(long followeeId, UserFilterDto filter){
        validateUserId(followeeId);
        return applyFilter(subscriptionRepository.findByFolloweeId(followeeId), filter);
    }

    public int getFollowingCount(long followerId){
        validateUserId(followerId);
        return subscriptionRepository.findFolloweesAmountByFollowerId(followerId);
    }

    private List<UserDto> applyFilter(Stream<User> users, UserFilterDto DtoFilters){

        List<UserDto> result = userFilters.stream()
                .filter(filter -> filter.isApplicable(DtoFilters))
                .flatMap(filter -> filter.apply(users, DtoFilters))
                .map(userMapper::userToDto)
                .toList();
        return result;
    }

    private void validateFollower(long followerId, long followeeId){
        validateUserId(followerId);
        validateUserId(followeeId);
        if(followerId == followeeId){
            throw new DataValidationException(ErrorMessages.SAME_ID);
        }
    }

    private void validateUserId(long userId){
        if(userId <= 0){
            throw new IllegalArgumentException(ErrorMessages.NEGATIVE_ID);
        }
    }
}