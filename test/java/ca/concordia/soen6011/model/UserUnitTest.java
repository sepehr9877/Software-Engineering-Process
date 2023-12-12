package ca.concordia.soen6011.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserUnitTest {

	public static final int USER_ID = 4;
	public static final String USER_USERNAME = "TheTestMaster";
	public static final String USER_FIRSTNAME = "Testy";
	public static final String USER_LASTNAME = "McTesterface";
	public static final String USER_EMAIL = "testy@aol.com";
	public static final String USER_PASSWORD = "qwerty";
	public static final String COMPANY = "Nortel";

	private Resume resume;
	private User user;
	
	@BeforeEach
	public void init() {

		resume = new Resume();
		user = new User();
    	
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);
		user.setFirstname(USER_FIRSTNAME);
		user.setLastname(USER_LASTNAME);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);
		user.setResume(resume);
	}

    @Test
    void testMembers() {

        assertEquals(USER_ID, user.getId());
        assertEquals(USER_USERNAME, user.getUsername());
        assertEquals(USER_FIRSTNAME, user.getFirstname());
        assertEquals(USER_LASTNAME, user.getLastname());
        assertEquals(USER_EMAIL, user.getEmail());
        assertEquals(USER_PASSWORD, user.getPassword());
        assertEquals(resume, user.getResume());
    }
	
    @Test
    void testCompany() {

        assertEquals(null, user.getCompany());

        Experience e = new Experience();
        e.setCompany(COMPANY);
        
    	user.setResume(null);    	
        assertEquals(null, user.getCompany());
    }
}
