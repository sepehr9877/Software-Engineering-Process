package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.User;

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
public class UserRepositoryIntegrationTest {
	
	public final static Integer User_ID = 1;
	public final static String User_USERNAME = "testuser";
	public final static String User_FIRSTNAME = "Test Name";
	public final static String User_LASTNAME = "Test Description";

	@Autowired
	private DataSource dataSource;

    @Autowired
	private UserRepository UserRepository;
	
	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(UserRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<User> UserResult = UserRepository.findById(User_ID);
		assertTrue(UserResult.isPresent());
		
		User User = UserResult.get();
		assertNotNull(User);
		
		User.setFirstname(User_FIRSTNAME);
		User.setLastname(User_LASTNAME);
		UserRepository.save(User);

		Optional<User> User2Result = UserRepository.findByUsername(User_USERNAME);
		assertTrue(User2Result.isPresent());

		User User2 = User2Result.get();
		assertNotNull(User2);

		assertEquals(User_FIRSTNAME, User2.getFirstname());
		assertEquals(User_LASTNAME, User2.getLastname());
	}
}