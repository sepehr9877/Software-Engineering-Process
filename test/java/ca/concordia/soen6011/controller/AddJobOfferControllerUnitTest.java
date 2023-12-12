package ca.concordia.soen6011.controller;


import ca.concordia.soen6011.model.*;
import ca.concordia.soen6011.repository.CompanyRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddJobOfferControllerUnitTest {

    private static final int USER_ID = 1;
    private static final String USER_USERNAME = "TheTestUser";
    private static final int JOB_ID = 2;
    private static final int COMPANY_ID = 2;


    @InjectMocks
    private AddJobOfferController controller;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;


    @Mock
    private Model model;

    @Mock
    private UserDetails userDetails;

    @Mock
    private Authentication authentication;


    @BeforeEach
    void beforeEach() {
    }

    @Test
    void testSaveJobOffer(){
        LocalDate published = LocalDate.of(2023, 8, 1);
        LocalDate deadline = LocalDate.of(2023, 8, 31);
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        User user = setupUser();
        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        // Call the method to test
        String result = controller.saveJobOffer("Job Title", "Job Description", 5, "Company Inc.", published, deadline,
                50000, 100000, userDetails);

        assertEquals("redirect:/company/myJobOffers", result);
        verify(userRepository, times(1)).findByUsername(USER_USERNAME);
        verify(jobOfferRepository, times(1)).save(any());
    }

    @Test
    void testMyJobOffers(){

        User user = setupUser();
        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);

        List<Joboffers> listJobOffers = new ArrayList<>();
        Joboffers joboffers = setupJobOffer();
        listJobOffers.add(joboffers);

        // Mock authentication.getName() to return a valid username
        when(authentication.getName()).thenReturn(USER_USERNAME);

        // Mocking the behavior of userRepository
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        // Mocking the behavior of jobOfferRepository
        when(jobOfferRepository.findByCompanyId(company.getId())).thenReturn(listJobOffers);

        // Call the method to be tested
        String result = controller.myJobOffers(model, authentication);

        // Assertions
        assertEquals("my-job-offers", result);

        // Verify the model attributes
        verify(model).addAttribute("myJobOffers", listJobOffers);
    }

    @Test
    void testDeleteJobOffer() {
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        User user = setupUser();
        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        user.setCompany(company);
        when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

        Joboffers jobOffer = setupJobOffer();
        jobOffer.setStatus(Joboffers.JobOfferStatus.PENDING);
        Optional<Joboffers> jobOfferOptional = Optional.of(jobOffer);
        when(jobOfferRepository.findById(JOB_ID)).thenReturn(jobOfferOptional);

        String result = controller.deleteJobOffer(JOB_ID, userDetails);

        // Assert that the job offer status is changed to REJECTED
        verify(jobOfferRepository).save(argThat(joboffers -> jobOffer.getStatus() == Joboffers.JobOfferStatus.REJECTED));

        // Assert that the correct redirection URL is returned
        assertEquals("redirect:/company/myJobOffers", result);
    }

    @Test
    void testEditJobOffer() {
        // Mock the behavior of the jobOfferRepository
        int jobId = 1;
        Joboffers jobOffer = new Joboffers();
        jobOffer.setId(jobId);
        when(jobOfferRepository.findById(jobId)).thenReturn(Optional.of(jobOffer));

        // Call the controller method
        String result = controller.editJobOffer(jobId, model, userDetails);

        // Assertions
        assertEquals("edit-job-page", result);
        verify(model).addAttribute(eq("jobOffer"), any(Joboffers.class));
    }

    @Test
    void testEdit_update_JobOffer() {

        Integer jobOfferId = JOB_ID;
        String title = "Software Engineer";
        String description = "Job description";
        int staff = 5;
        String companyTxt = "Acme Inc.";
        LocalDate published = LocalDate.of(2023, 8, 1);
        LocalDate deadline = LocalDate.of(2023, 8, 31);
        int lowsalery = 50000;
        int highsalery = 80000;
        when(userDetails.getUsername()).thenReturn(USER_USERNAME);

        User user = setupUser();
        Company company = new Company();
        company.setId(COMPANY_ID);
        company.setUser(user);
        company.setName("Old Company");
        user.setCompany(company);

        Joboffers existingJobOffer = new Joboffers();
        existingJobOffer.setId(jobOfferId);
        existingJobOffer.setCompany(company);

        when(jobOfferRepository.findById(jobOfferId)).thenReturn(Optional.of(existingJobOffer));
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        // Call the method
        String result = controller.editJobOffer(jobOfferId, title, description, staff, companyTxt, published,
                deadline, lowsalery, highsalery, userDetails);

        // Verify the save method is called on the repository
        verify(jobOfferRepository).save(existingJobOffer);

        assertEquals("redirect:/company/myJobOffers", result);
    }

    private User setupUser() {

        User c = new User();
        c.setId(USER_ID);
        c.setUsername(USER_USERNAME);
        c.setResume(new Resume());
//        c.setCompany(setupCompany());
        return c;
    }

    private Joboffers setupJobOffer() {

        Joboffers j = new Joboffers();
        j.setId(JOB_ID);
        return j;
    }

}
