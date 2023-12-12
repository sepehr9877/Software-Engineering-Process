package ca.concordia.soen6011.acceptance;

import java.util.ArrayList;

import ca.concordia.soen6011.controller.JobController;
import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Company;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.UserRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class TrackApplicationsAcceptanceTest {

	private static final String USER_A_USERNAME = "TheTestMaster";
	private static final String USER_B_USERNAME = "TheEmployer";

	@Autowired
	private MockMvc mockMvc;

    @Autowired
	private UserRepository userRepository;

    @Autowired
	private JobOfferRepository jobOfferRepository;

    @Autowired
	private ApplicationRepository applicationRepository;

    private Applications application;
    private Company company;
    private User userA;
    private User userB;
    private Joboffers jobOffer;
    
	@BeforeEach
    void beforeEach() {

		userA = new User();
		userA.setUsername(USER_A_USERNAME);

		userB = new User();
		userB.setUsername(USER_B_USERNAME);

		company = new Company();
		company.setUser(userB);
		userB.setCompany(company);
		
		jobOffer = new Joboffers();
		jobOffer.setCompany(company);
		
		ArrayList<Joboffers> list = new ArrayList<Joboffers>();
		list.add(jobOffer);
		company.setJoboffers(list);

        try{
        	userRepository.save(userA);
        	userRepository.save(userB);
        	jobOfferRepository.save(jobOffer);
        }
        catch (Exception e){}
	}
	
	@AfterEach
    public void afterEach() {

		try {
			applicationRepository.deleteById(application.getId());
        	jobOfferRepository.deleteById(jobOffer.getId());
			userRepository.deleteById(userA.getId());
			userRepository.deleteById(userB.getId());
		}
		catch (Exception e) {}
	}
	
    @Test
    @WithMockUser(USER_A_USERNAME)
    void testShowApplicationList() throws Exception {
    	
    	MvcResult mvcResult = mockMvc.perform(get("/jobs/applications")
			.with(csrf()))
			.andExpect(request().asyncStarted())
			.andReturn();
		
		mockMvc.perform(asyncDispatch(mvcResult))
			.andExpect(status().isOk())
			.andExpect(view().name(JobController.JOBS_APPLICATIONS_VIEW))
			.andExpect(model().attributeExists(JobController.APPLICATION_LIST))
			.andReturn();
    }
	
    @Test
    @WithMockUser(USER_A_USERNAME)
    void testShowApplication() throws Exception {
    	
    	Applications application = new Applications();
    	application.setUser(userA);
    	application.setJoboffers(jobOffer);

        try{
        	applicationRepository.save(application);
        }
        catch (Exception e){}
    	
		MvcResult mvcResult = mockMvc.perform(get("/jobs/applications/view/" + application.getId())
			.with(csrf()))
			.andExpect(request().asyncStarted())
			.andReturn();
		
		mockMvc.perform(asyncDispatch(mvcResult))
			.andExpect(status().isOk())
			.andExpect(view().name(JobController.JOBS_APPLICATIONS_VIEW_VIEW))
			.andExpect(model().attributeExists(JobController.APPLICATION_DATA))
			.andReturn();
    }
}
