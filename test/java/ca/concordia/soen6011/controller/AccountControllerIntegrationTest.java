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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Import(WebSecurityConfig.class)
@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerIntegrationTest {
    private final String USERNAME = "TESTUSERNAEM";
    private final String FIRSTNAME ="TESTFirstName";
    private final String LASTNAME ="TESTlastName";
    private final String EMAIL ="TESTEMAIL@MAIL.com";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    UserDetails userDetails;

    User u;

    @BeforeEach
    private void init(){
        u = new User();
        u.setFirstname(FIRSTNAME);
        u.setLastname(LASTNAME);
        u.setUsername(USERNAME);
        u.setEmail(EMAIL);
    }


    @Test
    @WithMockUser(USERNAME)
    void testAccountPage() throws Exception {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(u));
        MvcResult mvcResult = mockMvc.perform(get("/account/"+USERNAME))
                .andExpect(status().isOk())
                .andExpect(view().name("my_account"))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertTrue(actualResponseBody.contains("settings"));
    }

    @Test
    @WithMockUser(USERNAME)
    void testUpdatingInformation() throws Exception {

        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(u));

        // 1. Verifying HTTP Request Matching
        mockMvc.perform(post("/account/updateMyInfo")
                        .param("firstName", "NEW")
                        .param("lastName", LASTNAME)
                        .param("email", EMAIL)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/account/" + USERNAME))
                .andReturn();

        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("NEW", u.getFirstname());
    }
}
