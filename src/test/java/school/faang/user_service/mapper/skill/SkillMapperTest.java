package school.faang.user_service.mapper.skill;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.dto.skill.UserSkillGuaranteeDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.UserSkillGuarantee;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SkillMapperTest {
    @Spy
    private SkillMapperImpl skillMapper;
    private Skill skill;
    private SkillDto skillDto;
    private SkillDto updatedSkillDto;

    @BeforeEach
    void setUp() {
        UserSkillGuarantee userSkillGuarantee1 = UserSkillGuarantee.builder()
                .id(31L)
                .build();

        skill = Skill.builder()
                .id(30L)
                .title("Programming")
                .guarantees(List.of(userSkillGuarantee1))
                .build();

        skillDto = SkillDto.builder()
                .id(30L)
                .title("Programming")
                .guarantees(List.of(
                        UserSkillGuaranteeDto.builder().id(31L).build()
                )).build();

        updatedSkillDto = SkillDto.builder()
                .id(1L)
                .title("Updated Programming")
                .build();
    }

    @Test
    @DisplayName("Test mapping from Skill to SkillDto")
    void testToDto() {
        SkillDto mappedSkillDto = skillMapper.toDto(skill);
        assertEquals(skillDto, mappedSkillDto);
    }

    @Test
    @DisplayName("Test mapping from SkillDto to Skill")
    void testToEntity() {
        Skill mappedSkill = skillMapper.toEntity(skillDto);
        assertEquals(skill, mappedSkill);
    }

    @Test
    @DisplayName("Test mapping list of Skills to list of SkillDtos")
    void testToListSkillsDto() {
        List<Skill> skills = List.of(skill);
        List<SkillDto> mappedSkillDtos = skillMapper.toListSkillsDto(skills);
        assertEquals(List.of(skillDto), mappedSkillDtos);
    }

    @Test
    @DisplayName("Test mapping list of SkillDtos to list of Skills")
    void testToListSkillsEntity() {
        List<SkillDto> skillDtos = List.of(skillDto);
        List<Skill> mappedSkills = skillMapper.toListSkillsEntity(skillDtos);
        assertEquals(List.of(skill), mappedSkills);
    }

    @Test
    @DisplayName("Test updating SkillDto from Skill")
    void testUpdateSkill() {
        skillMapper.updateSkill(updatedSkillDto, skill);
        assertEquals(updatedSkillDto.getId(), skill.getId());
        assertEquals(updatedSkillDto.getTitle(), skill.getTitle());
    }
}