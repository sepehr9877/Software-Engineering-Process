package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.User;

import java.time.LocalDate;
import java.util.List;
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
public class ApplicationRepositoryIntegrationTest {
	
	public final static Integer APPLICATION_ID = 1;
	public final static Applications.ApplicationStatus APPLICATION_STATUS = Applications.ApplicationStatus.ACCEPTED;
	public final static LocalDate APPLICATION_DATE = LocalDate.now();
	public final static String USERNAME = "testuser";

	@Autowired
	private DataSource dataSource;

    @Autowired
	private ApplicationRepository applicationRepository;

    @Autowired
	private UserRepository userRepository;
	
	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(applicationRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<Applications> applicationResult = applicationRepository.findById(APPLICATION_ID);
		assertTrue(applicationResult.isPresent());
		
		Applications application = applicationResult.get();
		assertNotNull(application);
		
		application.setStatus(APPLICATION_STATUS);
		application.setDateapplied(APPLICATION_DATE);
		applicationRepository.save(application);

		Optional<Applications> application2Result = applicationRepository.findById(APPLICATION_ID);
		assertTrue(application2Result.isPresent());

		Applications application2 = application2Result.get();
		assertNotNull(application2);

		assertEquals(APPLICATION_STATUS, application2.getStatus());
		assertEquals(APPLICATION_DATE, application2.getDateapplied());
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testfindByUser(){

		Optional<User> userOptional = userRepository.findByUsername(USERNAME);
		assertTrue(userOptional.isPresent());
		
		List<Applications> applicationList = applicationRepository.findByUser(userOptional.get());
		assertEquals(1, applicationList.size());
		assertEquals(1, applicationList.size());
		assertEquals(1, applicationList.size());		
	}

	
	@Test
	@Sql("classpath:/testData.sql")
	void testfindByUsername(){

		List<Applications> applicationList = applicationRepository.findByUserUsername(USERNAME);
		assertEquals(1, applicationList.size());
		assertEquals(1, applicationList.size());
		assertEquals(1, applicationList.size());		
	}
}
