package ca.concordia.soen6011.controller;


import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrowseJobOffersControllerUnitTest {

    private static final int USER_ID = 4;
    private static final String USER_USERNAME = "TheTestUser";
    private static final int JOB_ID = 2;
    private static final String JOBSTR_ID = "2";

    @InjectMocks
    private BrowseJobOffersController controller;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationRepository applicationRepository;


    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;


    @BeforeEach
    void beforeEach() {
    }

    @Test
    void testBrowseJobOffers() {
        List<Joboffers> listJobOffers = new ArrayList<>();
        Joboffers joboffers = setupJobOffer();
        listJobOffers.add(joboffers);
        when(jobOfferRepository.findAllByStatus(Joboffers.JobOfferStatus.ACCEPTED)).thenReturn(listJobOffers);
        String result = controller.browseJobOffers(model);

        // Assertions
        assertEquals("job_offer_list", result);
        verify(model, times(1)).addAttribute("jobOffers", listJobOffers);

    }

    @Test
    void testShowJobOfferDetails() {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Joboffers jobOffer = new Joboffers();
        jobOffer.setId(JOB_ID);
        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));

        User user = setupUser();
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        Applications applicationExists = new Applications();
        when(applicationRepository.findByJoboffersIdAndUserId(JOB_ID, user.getId())).thenReturn(applicationExists);

        // Call the method to test
        String result = controller.showJobOfferDetails(JOB_ID, model, userDetails);

        // Assertions
        assertEquals("job_offer_details", result);

        verify(model, times(1)).addAttribute("jobOffer", jobOffer);

    }

    @Test
    void testApplyJobOffer(){
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        Joboffers jobOffer = new Joboffers();
        jobOffer.setId(JOB_ID);
        when(jobOfferRepository.findById(JOB_ID)).thenReturn(Optional.of(jobOffer));

        User user = setupUser();
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        // Call the method to test
        String result = controller.applyJobOffer(JOBSTR_ID, model, userDetails);

        // Verify that the repositories are called appropriately
        verify(jobOfferRepository, times(1)).findById(Integer.parseInt(JOBSTR_ID));
        verify(userRepository, times(1)).findByUsername(USER_USERNAME);
        verify(applicationRepository, times(1)).save(any());

        // Assertions
        assertEquals("redirect:/applicant/browseJobOffers", result);
    }

    private User setupUser() {

        User c = new User();
        c.setId(USER_ID);
        c.setUsername(USER_USERNAME);
        c.setResume(new Resume());
        return c;
    }

    private Joboffers setupJobOffer() {

        Joboffers j = new Joboffers();
        j.setId(JOB_ID);
        return j;
    }

}
