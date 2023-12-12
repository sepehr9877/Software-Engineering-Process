package ca.concordia.soen6011.service;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

	private static final int USER_ID = 47;
	private static final String USER_USERNAME = "testuser";
	private static final String USER_FIRSTNAME = "first name";
	private static final String USER_LASTNAME = "last name";

	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserServiceImpl userService;
	
	private User user;
	
	@BeforeEach
	void beforeEach() {
		
		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);
		user.setFirstname(USER_FIRSTNAME);
		user.setLastname(USER_LASTNAME);
	}
	
	@Test
	void testFindUser() {
		
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

    	assertDoesNotThrow(() -> {    	

    		Optional<User> userOptional = userService.findUser(USER_USERNAME).get();
    		assertTrue(userOptional.isPresent());
    		
    		User u =  userOptional.get();
    		assertEquals(USER_ID, u.getId());
    		assertEquals(USER_USERNAME, u.getUsername());
    		assertEquals(USER_FIRSTNAME, u.getFirstname());
    		assertEquals(USER_LASTNAME, u.getLastname());
    	});
	}
	
}
