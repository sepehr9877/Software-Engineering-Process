package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.repository.ApplicationRepository;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import java.util.Optional;
import static org.mockito.Mockito.when;


@WebMvcTest(BrowseJobOffersController.class)
@ExtendWith(MockitoExtension.class)
public class BrowseJobOffersControllerIntegrationTest {

    private static final int USER_ID = 4;
    private static final String USER_USERNAME = "TheTestMaster";
    private static final int JOB_ID = 1;
    private static final String JOB_ID_SRT = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobOfferRepository jobOfferRepository;

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private UserRepository userRepository;


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
    void testIndexView() throws Exception {
        Joboffers jobOffer = setupJobOffer();
        ArrayList<Joboffers> list = new ArrayList<Joboffers>();
        list.add(jobOffer);

        // Mock the behavior of jobOfferRepository
        when(jobOfferRepository.findAllByStatus(Joboffers.JobOfferStatus.ACCEPTED))
                .thenReturn(list);

        // Perform the GET request and assert the result
        mockMvc.perform(get("/applicant/browseJobOffers"))
                .andExpect(status().isOk())
                .andExpect(view().name("job_offer_list"))
                .andExpect(model().attributeExists("jobOffers"));

        // Verify that jobOfferRepository method was called
        verify(jobOfferRepository, times(1)).findAllByStatus(Joboffers.JobOfferStatus.ACCEPTED);
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testShowJobOfferDetails() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Joboffers jobOffer = setupJobOffer();

        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(new User()));

        when(applicationRepository.findByJoboffersIdAndUserId(JOB_ID, user.getId())).thenReturn(new Applications());

        mockMvc.perform(get("/applicant/jobOfferDetails/{id}", JOB_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("job_offer_details"))
                .andExpect(model().attributeExists("jobOffer", "alreadyApplied"));

        verify(jobOfferRepository, times(2)).findById(JOB_ID);
        verify(userRepository, times(1)).findByUsername(USER_USERNAME);
    }

    @Test
    @WithMockUser(USER_USERNAME)
    void testApplyJobOffer() throws Exception {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Joboffers jobOffer = setupJobOffer();

        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/applicant/applyJobOffer/" + JOB_ID_SRT))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/applicant/browseJobOffers"));

        verify(applicationRepository, times(1)).save(any());
    }

    private Joboffers setupJobOffer() {

        Joboffers j = new Joboffers();
        j.setId(JOB_ID);
        return j;
    }
}
