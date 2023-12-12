package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.Education;

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
public class EducationRepositoryIntegrationTest {
	
	public final static Integer EDUCATION_ID = 1;
	public final static String EDUCATION_DEGREE = "Test Degree";
	public final static String EDUCATION_UNIVERSITY = "Test University";

	@Autowired
	private DataSource dataSource;

    @Autowired
	private EducationRepository educationRepository;
	
	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(educationRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<Education> educationResult = educationRepository.findById(EDUCATION_ID);
		assertTrue(educationResult.isPresent());
		
		Education education = educationResult.get();
		assertNotNull(education);
		
		education.setDegree(EDUCATION_DEGREE);
		education.setUniversity(EDUCATION_UNIVERSITY);
		educationRepository.save(education);

		Optional<Education> education2Result = educationRepository.findById(EDUCATION_ID);
		assertTrue(education2Result.isPresent());

		Education education2 = education2Result.get();
		assertNotNull(education2);

		assertEquals(EDUCATION_DEGREE, education2.getDegree());
		assertEquals(EDUCATION_UNIVERSITY, education2.getUniversity());
	}
}
