package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.*;
import ca.concordia.soen6011.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import ca.concordia.soen6011.repository.ApplicationRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Collections;
import java.util.Optional;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AddJobOfferController.class)
@ExtendWith(MockitoExtension.class)
public class AddJobOfferControllerIntegrationTest {
    private static final int USER_ID = 4;
    private static final String USER_USERNAME = "TheTestMaster";
    private static final int JOB_ID = 1;
    private static final String JOB_ID_SRT = "1";
    private static final int COMPANY_ID = 2;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferRepository jobOfferRepository;

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CompanyRepository companyRepository;

    @MockBean
    private Authentication authentication;


    private User user;

    @MockBean
    UserDetails userDetails;

    @BeforeEach
    void init() {

        user = new User();
        user.setId(USER_ID);
        user.setUsername(USER_USERNAME);
        user.setResume(new Resume());
        user.getResume().setUser(user);
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testSaveJobOffer() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        Joboffers jobOffer = setupJobOffer();

        when(jobOfferRepository.save(any(Joboffers.class))).thenReturn(jobOffer);

// Prepare a POST request
        mockMvc.perform(post("/company/saveJobOffer")
                        .param("title", "Test Job")
                        .param("description", "Test job description")
                        .param("staff", "5")
                        .param("company", "Test Company")
                        .param("published", LocalDate.now().toString())
                        .param("deadline", LocalDate.now().plusDays(30).toString())
                        .param("lowsalery", "50000")
                        .param("highsalery", "80000")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/myJobOffers"));
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testMyJobOffers() throws Exception {
        when(authentication.getName()).thenReturn(USER_USERNAME);
        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);

        Joboffers jobOffer = setupJobOffer();
        jobOffer.setStatus(Joboffers.JobOfferStatus.ACCEPTED);
        ArrayList<Joboffers> list = new ArrayList<Joboffers>();
        list.add(jobOffer);

        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
        when(jobOfferRepository.findByCompanyId(company.getId())).thenReturn(list);

        mockMvc.perform(get("/company/myJobOffers"))
                .andExpect(status().isOk())
                .andExpect(view().name("my-job-offers"))
                .andExpect(model().attributeExists("myJobOffers"));

        // Verify repository method calls
        verify(userRepository, times(1)).findByUsername(anyString());
        verify(jobOfferRepository, times(1)).findByCompanyId(anyInt());
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testDeleteJobOffer() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);

        Joboffers jobOffer = setupJobOffer();
        jobOffer.setStatus(Joboffers.JobOfferStatus.ACCEPTED);

        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));

        // Perform the request and assertions
        mockMvc.perform(get("/company/deleteJobOffer/{id}", JOB_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/myJobOffers"));

        // Verify changes
        assertEquals(Joboffers.JobOfferStatus.REJECTED, jobOffer.getStatus());
        verify(jobOfferRepository, times(1)).save(jobOffer);
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testEditJobOffer() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);

        Joboffers jobOffer = setupJobOffer();
        jobOffer.setStatus(Joboffers.JobOfferStatus.ACCEPTED);

        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        // Perform the request and assertions
        mockMvc.perform(get("/company/editJobOffer/{id}", JOB_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("edit-job-page"))
                .andExpect(model().attributeExists("jobOffer"))
                .andExpect(model().attribute("jobOffer", jobOffer));

        // Verify interactions
        verify(jobOfferRepository, times(1)).findById(JOB_ID);
        verifyNoMoreInteractions(jobOfferRepository);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testEdit_update_JobOffer() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        Joboffers jobOffer = setupJobOffer();
        when(jobOfferRepository.save(any(Joboffers.class))).thenReturn(jobOffer);

        // Prepare a POST request
        mockMvc.perform(post("/company/editJobOffer")
                        .param("jobOfferId", JOB_ID_SRT)
                        .param("title", "Test Job")
                        .param("description", "Test job description")
                        .param("staff", "5")
                        .param("company", "Test Company")
                        .param("published", LocalDate.now().toString())
                        .param("deadline", LocalDate.now().plusDays(30).toString())
                        .param("lowsalery", "50000")
                        .param("highsalery", "80000")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/company/myJobOffers"));

        // Verify interactions and assertions
        verify(jobOfferRepository).findById(eq(JOB_ID));
    }

    private Joboffers setupJobOffer() {

        Joboffers j = new Joboffers();
        j.setId(JOB_ID);
        return j;
    }
}
