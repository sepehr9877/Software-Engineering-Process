package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
class TrackJobPostUnitTest {

    @InjectMocks
    private TrackJobPost controller;

    @Mock
    private JobOfferRepository jobOfferRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private Model model;

    @Test
    void testViewByCompanyWithoutCompanyParameter() throws Exception {
        // Stubbing inside the test method where it's used
        when(jobOfferRepository.findDistinctCompanies()).thenReturn(Arrays.asList("CompanyA", "CompanyB"));

        String result = controller.viewByCompany(null, model);

        assertEquals("track-job-post", result);
        verify(model).addAttribute("companies", Arrays.asList("CompanyA", "CompanyB"));
        verifyNoMoreInteractions(model);
    }

    @Test
    void testViewByCompanyWithCompanyParameter() throws Exception {
        // Stubbing inside the test method where it's used
        when(jobOfferRepository.findDistinctCompanies()).thenReturn(Arrays.asList("CompanyA", "CompanyB"));

        Joboffers jobOffer = new Joboffers();
        // Set properties for jobOffer if needed
        when(jobOfferRepository.findByCompanyName("CompanyA")).thenReturn(Arrays.asList(jobOffer));

        String result = controller.viewByCompany("CompanyA", model);

        assertEquals("track-job-post", result);
        verify(model).addAttribute("companies", Arrays.asList("CompanyA", "CompanyB"));
        verify(model).addAttribute("jobOffers", Arrays.asList(jobOffer));
    }

    @Test
    void testViewApplicants() throws Exception {
        Applications application = new Applications();
        // Set properties for application if needed
        when(applicationRepository.findByJoboffersId(1L)).thenReturn(Arrays.asList(application));

        String result = controller.viewApplicants(1L, model);

        assertEquals("view-applicants", result);
        verify(model).addAttribute("applications", Arrays.asList(application));
    }
}

