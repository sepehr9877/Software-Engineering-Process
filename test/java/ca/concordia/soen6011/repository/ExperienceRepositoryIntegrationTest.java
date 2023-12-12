package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.Experience;

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
public class ExperienceRepositoryIntegrationTest {
	
	public final static Integer EXPERIENCE_ID = 1;
	public final static String EXPERIENCE_COMPANY = "Test Company";
	public final static String EXPERIENCE_DESCRIPTION = "Test Description";

	@Autowired
	private DataSource dataSource;

    @Autowired
	private ExperienceRepository experienceRepository;
	
	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(experienceRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<Experience> experienceResult = experienceRepository.findById(EXPERIENCE_ID);
		assertTrue(experienceResult.isPresent());
		
		Experience experience = experienceResult.get();
		assertNotNull(experience);
		
		experience.setCompany(EXPERIENCE_COMPANY);
		experience.setDescription(EXPERIENCE_DESCRIPTION);
		experienceRepository.save(experience);

		Optional<Experience> experience2Result = experienceRepository.findById(EXPERIENCE_ID);
		assertTrue(experience2Result.isPresent());

		Experience experience2 = experience2Result.get();
		assertNotNull(experience2);

		assertEquals(EXPERIENCE_COMPANY, experience2.getCompany());
		assertEquals(EXPERIENCE_DESCRIPTION, experience2.getDescription());
	}
}
