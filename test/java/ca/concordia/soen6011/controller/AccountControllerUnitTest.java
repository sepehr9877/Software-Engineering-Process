package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerUnitTest {
    private final String USERNAME = "TESTUSERNAEM";
    private final String FIRSTNAME ="TESTFirstName";
    private final String LASTNAME ="TESTlastName";
    private final String EMAIL ="TESTEMAIL@MAIL.com";
    @InjectMocks
    AccountController accountController;
    @Mock
    UserRepository userRepository;
    @Mock
    private UserDetails userDetails;
    @Mock
    Model model;

    @Test
    void testAccountAccessFailed(){
        when(userDetails.getUsername()).thenReturn("SomethingElse");
        String result = accountController.goToMyAccount(USERNAME, userDetails, model);
        assertEquals("redirect:/", result);
    }

    @Test
    void testAccountAccessSuccess(){
        User u = createTestUser();
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(u));
        String res = accountController.goToMyAccount(USERNAME, userDetails, model);
        assertEquals("my_account", res);
    }

    @Test
    void testUpdateAccountInfo(){
        User u = createTestUser();
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(u));
        assertEquals("redirect:/account/" + USERNAME,
                accountController.updateInfo("Changed", LASTNAME, EMAIL, userDetails));
        assertEquals("redirect:/account/" + USERNAME,
                accountController.updateInfo(FIRSTNAME, "CHANGED", EMAIL, userDetails));
        assertEquals("redirect:/account/" + USERNAME,
                accountController.updateInfo(FIRSTNAME, LASTNAME, "CHANGED", userDetails));
    }

    private User createTestUser(){
        User u = new User();
        u.setUsername(USERNAME);
        u.setFirstname(FIRSTNAME);
        u.setLastname(LASTNAME);
        u.setEmail(EMAIL);
        return u;
    }
}
