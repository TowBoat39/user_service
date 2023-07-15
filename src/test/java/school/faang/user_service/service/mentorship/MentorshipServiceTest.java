package school.faang.user_service.service.mentorship;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.UserNotFoundException;
import school.faang.user_service.repository.mentorship.MentorshipRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MentorshipServiceTest {
    private static final long MENTOR_ID = 1L;
    private static final long MENTEE_ID = 2L;
    private static final long INCORRECT_USER_ID = 0L;
    @Mock
    private MentorshipRepository mentorshipRepository;
    @InjectMocks
    private MentorshipService mentorshipService;

    @BeforeEach
    void setUp() {
        when(mentorshipRepository.findUserById(INCORRECT_USER_ID))
                .thenReturn(Optional.empty());

        User mentor = User.builder().mentees(List.of(new User(), new User())).build();
        when(mentorshipRepository.findUserById(MENTOR_ID))
                .thenReturn(Optional.of(mentor));

        User mentee = User.builder().mentors(List.of(new User())).build();
        when(mentorshipRepository.findUserById(MENTEE_ID))
                .thenReturn(Optional.of(mentee));
    }

    @Test
    void getMentees_shouldMatchMenteesSize() {
        List<User> mentees = mentorshipService.getMentees(MENTOR_ID);
        assertEquals(2, mentees.size());
    }

    @Test
    void getMentees_shouldInvokeFindByIdMethod() {
        mentorshipService.getMentees(MENTOR_ID);
        verify(mentorshipRepository).findUserById(MENTOR_ID);
    }

    @Test
    void getMentees_shouldThrowException() {
        assertThrows(UserNotFoundException.class,
                () -> mentorshipService.getMentees(INCORRECT_USER_ID),
                "Invalid user id");
    }

    @Test
    void getMentors_shouldMatchMenteesSize() {
        List<User> mentees = mentorshipService.getMentors(MENTEE_ID);
        assertEquals(1, mentees.size());
    }

    @Test
    void getMentors_shouldInvokeFindByIdMethod() {
        mentorshipService.getMentors(MENTEE_ID);
        verify(mentorshipRepository).findUserById(MENTEE_ID);
    }

    @Test
    void getMentors_shouldThrowException() {
        assertThrows(UserNotFoundException.class,
                () -> mentorshipService.getMentors(INCORRECT_USER_ID),
                "Invalid user id");
    }
}