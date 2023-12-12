package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerUnitTest {
    private final String USERNAME = "TestUser";
    private final String EMAIL= "TestEmail";
    private final String PASSWORD = "testing123";
    private final String FIRSTNAME = "Test";
    private final String LASTNAME = "Tester";
    @InjectMocks
    RegistrationController registrationController;

    @Mock
    UserRepository userRepository;
    @Mock
    HttpServletRequest httpRequest;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    Authentication authentication;

    @Test
    void testOpenRegisterPage(){
        String result = registrationController.OpenRegisterPage();
        assertEquals("registration_page", result);
    }


    @Test
    void testRegister() throws ServletException {
        User u = new User();
        u.setUsername(USERNAME);
        u.setEmail(EMAIL);
        u.setFirstname(FIRSTNAME);
        u.setLastname(LASTNAME);
        u.setPassword(PASSWORD);

        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(u));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD);
        authToken.setDetails(new WebAuthenticationDetails(httpRequest));
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);

        ModelAndView modelAndView = registrationController.register(USERNAME,EMAIL ,FIRSTNAME, LASTNAME, PASSWORD
                ,httpRequest);

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("redirect:/login", modelAndView.getViewName());

    }

    @Test
    void testCheckUsername(){
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);
        ResponseEntity<Boolean> userExists = registrationController.checkUsername(USERNAME);
        assertEquals(true, userExists.getBody());

        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        ResponseEntity<Boolean> userNotExists = registrationController.checkUsername(USERNAME);
        assertEquals(false, userNotExists.getBody());
    }
}
