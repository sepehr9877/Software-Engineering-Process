package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class LoginControllerUnitTest {

    private final String USERNAME = "TESTUSERNAEM";
    private final String EMAIL ="TESTEMAIL@MAIL.com";

    @InjectMocks
    LoginController loginController;

    @Mock
    Authentication authentication;

    @Mock
    UserRepository userRepository;

    @Mock
    Model model;

    @Test
    void testLoginPage(){
        String result = loginController.openLoginPage();
        assertEquals("new_login_page", result);
    }

    @Test
    void testSuccessLogin(){
        when(authentication.getName()).thenReturn(USERNAME);
        String result = loginController.loginsuccess(authentication);
        assertEquals("redirect:/profile/"+USERNAME,result);

    }

    @Test
    void testOpenForgotPassword(){
        String res = loginController.openForgotPassword();
        assertEquals("forgot_password", res);
    }

    @Test
    void testCheckEmail(){
    	User u = new User();
        u.setEmail(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(u));
        String res = loginController.checkEmail(EMAIL, model);
        assertEquals("forgot_password", res);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        res = loginController.checkEmail(EMAIL, model);
        assertEquals("forgot_password", res);
    }

    @Test
    void testUpdatePassword(){
        User u = new User();
        u.setEmail(EMAIL);

        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(u));
        String res = loginController.updatePassword(EMAIL, "NEWPASS");
        assertEquals("new_login_page", res);
        assertEquals("NEWPASS", u.getPassword());
    }
}
