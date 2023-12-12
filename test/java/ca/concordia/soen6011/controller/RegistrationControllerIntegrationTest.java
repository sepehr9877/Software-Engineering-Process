package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.configuration.WebSecurityConfig;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(WebSecurityConfig.class)
@WebMvcTest(RegistrationController.class)
@ExtendWith(MockitoExtension.class)
class RegistrationControllerIntegrationTest {
    private final String USERNAME = "TESTUSERNAEM";
    private final String FIRSTNAME ="TESTFirstName";
    private final String LASTNAME ="TESTlastName";
    private final String EMAIL ="TESTEMAIL@MAIL.com";
    private final String PASSWORD ="PreviousPassword";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    Authentication authentication;
    @MockBean
    HttpServletRequest httpServletRequest;
    @MockBean
    AuthenticationManager authenticationManager;

    User u;
    @BeforeEach
    private void init(){
        u = new User();
        u.setUsername(USERNAME);
        u.setEmail(EMAIL);
        u.setPassword(PASSWORD);
        u.setFirstname(FIRSTNAME);
        u.setLastname(LASTNAME);

        authentication.setAuthenticated(true);
    }

    @Test
    void testRegisterPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration_page"))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(actualResponseBody.contains("Whatever Job You Choose"));
    }

    @Test
    @WithMockUser(USERNAME)
    void testRegister() throws Exception{
        mockMvc.perform(post("/register")
                        .param("userName", USERNAME)
                        .param("email", EMAIL)
                        .param("firstName", FIRSTNAME)
                        .param("lastName",LASTNAME)
                        .param("password", PASSWORD)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andReturn();
    }

    @Test
    @WithMockUser(USERNAME)
    void testCheckUserName() throws Exception{
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/checkUsername")
                .param("username", USERNAME)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string("true"))
                .andReturn();
    }
}
