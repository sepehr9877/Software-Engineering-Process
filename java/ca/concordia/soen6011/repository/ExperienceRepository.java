package ca.concordia.soen6011.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.concordia.soen6011.model.Experience;

/** 
 * Experience are part of a Candidates' resume. They are saved in the repository.
 */
public interface ExperienceRepository extends JpaRepository<Experience,Integer> {
}
