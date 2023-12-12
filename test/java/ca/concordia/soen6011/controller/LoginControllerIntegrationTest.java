package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.configuration.WebSecurityConfig;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@WebMvcTest(LoginController.class)
@ExtendWith(MockitoExtension.class)
class LoginControllerIntegrationTest {
    private final String USERNAME = "TESTUSERNAEM";
    private final String EMAIL ="TESTEMAIL@MAIL.com";
    private final String PASSWORD ="PreviousPassword";
    private final String NewPASSWORD ="newPassword";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    Authentication authentication;

    User u;
    @BeforeEach
    private void init(){
        u = new User();
        u.setUsername(USERNAME);
        u.setEmail(EMAIL);
        u.setPassword(PASSWORD);
    }

    @Test
    void testLoginPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("new_login_page"))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(actualResponseBody.contains("Whatever Job You Choose"));
    }

    @Test
    @WithMockUser(USERNAME)
    void testLoginSuccess() throws Exception{
        when(authentication.getName()).thenReturn(USERNAME);
        // 1. Verifying HTTP Request Matching
        mockMvc.perform(get("/login-success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/profile/" + USERNAME))
                .andReturn();
    }


    @Test
    void testForgotPass() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot_password"))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(actualResponseBody.contains("Whatever Job You Choose"));
    }

    @Test
    @WithMockUser(USERNAME)
    void testCheckEmail() throws Exception {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(u));
        mockMvc.perform(post("/sendEmail")
                        .param("email", EMAIL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot_password"))
                .andReturn();
    }

    @Test
    @WithMockUser(USERNAME)
    void testWrongEmail() throws Exception{
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());
        mockMvc.perform(post("/sendEmail")
                        .param("email", EMAIL)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot_password"))
                .andReturn();
    }

    @Test
    @WithMockUser(USERNAME)
    void testUpdatePass() throws Exception{
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(u));
        mockMvc.perform(post("/updatePass")
                        .param("userEmail", EMAIL)
                        .param("password", NewPASSWORD)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("new_login_page"))
                .andReturn();

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(u.getPassword(), NewPASSWORD);
    }

}
