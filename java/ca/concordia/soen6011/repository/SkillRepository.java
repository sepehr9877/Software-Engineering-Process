package ca.concordia.soen6011.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.concordia.soen6011.model.Skill;

/** 
 * Skills are part of a Candidates' resume. They are saved in the repository.
 */
public interface SkillRepository extends JpaRepository<Skill,Integer> {
}
