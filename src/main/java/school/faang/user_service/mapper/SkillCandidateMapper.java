package school.faang.user_service.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillCandidateMapper {
    Skill toEntity(SkillCandidateDto skillCandidateDto);

    @Mapping(source = "skillDto", target = "skill")
    @Mapping(source = "count", target = "offersAmount")
    SkillCandidateDto toDTO(SkillDto skillDto, Long count);

}