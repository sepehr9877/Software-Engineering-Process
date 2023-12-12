package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.Skill;

import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase
public class SkillRepositoryIntegrationTest {
	
	public final static Integer SKILL_ID = 1;
	public final static String SKILL_NAME = "Test Name";
	public final static String SKILL_DESCRIPTION = "Test Description";

	@Autowired
	private DataSource dataSource;

    @Autowired
	private SkillRepository skillRepository;

	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(skillRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<Skill> skillResult = skillRepository.findById(SKILL_ID);
		assertTrue(skillResult.isPresent());
		
		Skill skill = skillResult.get();
		assertNotNull(skill);
		
		skill.setName(SKILL_NAME);
		skill.setDescription(SKILL_DESCRIPTION);
		skillRepository.save(skill);

		Optional<Skill> skill2Result = skillRepository.findById(SKILL_ID);
		assertTrue(skill2Result.isPresent());

		Skill skill2 = skill2Result.get();
		assertNotNull(skill2);

		assertEquals(SKILL_NAME, skill2.getName());
		assertEquals(SKILL_DESCRIPTION, skill2.getDescription());
	}
}