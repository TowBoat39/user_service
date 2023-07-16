package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.UserSkillGuarantee;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mappers.SkillMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserSkillGuaranteeRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final SkillOfferRepository skillOfferRepository;
    private final UserSkillGuaranteeRepository userSkillGuaranteeRepository;
    private final long MIN_SKILL_OFFERS = 3;

    public SkillDto create(SkillDto skill) {
        if (skillRepository.existsByTitle(skill.getTitle())) {
            throw new DataValidationException("This skill already exist");
        }
        Skill savedSkill = skillRepository.save(skillMapper.toEntity(skill));
        return skillMapper.toDTO(savedSkill);
    }

    public List<SkillDto> getUserSkills(long userId) {
        List<SkillDto> list = new ArrayList<>();
        for (Skill skill : skillRepository.findAllByUserId(userId)) {
            list.add(skillMapper.toDTO(skill));
        }
        return list;
    }

    public List<SkillCandidateDto> getOfferedSkills(long userId) {
        List<SkillCandidateDto> list = new ArrayList<>();
        for (Skill skill : skillRepository.findAllByUserId(userId)) {
            list.add(skillMapper.candidateToDTO(skill));
        }
        return list;
    }

    public SkillDto acquireSkillFromOffers(long skillId, long userId) {
        Optional<Skill> userSkill = skillRepository.findUserSkill(skillId, userId);

        if (userSkill.isEmpty()) {
            List<SkillOffer> allOffersOfSkill = skillOfferRepository.findAllOffersOfSkill(skillId, userId);
            if (allOffersOfSkill.size() >= MIN_SKILL_OFFERS) {
                skillRepository.assignSkillToUser(skillId, userId);
                addGuaranteeRepository(allOffersOfSkill);
                return skillMapper.toDTO(skillRepository.findById(skillId).orElseThrow(() -> new DataValidationException("This skill doesn't exist")));
            }
        }
        return skillMapper.toDTO(skillRepository.findById(skillId).orElseThrow(() -> new DataValidationException("This skill doesn't exist")));
    }

    private void addGuaranteeRepository(List<SkillOffer> allOffersOfSkill) {
        for (SkillOffer skillOffer : allOffersOfSkill) {
            User receiver = skillOffer.getRecommendation().getReceiver();
            User author = skillOffer.getRecommendation().getAuthor();
            userSkillGuaranteeRepository.save(UserSkillGuarantee.builder()
                    .user(receiver)
                    .guarantor(author)
                    .skill(skillOffer.getSkill())
                    .build());
        }
    }
}