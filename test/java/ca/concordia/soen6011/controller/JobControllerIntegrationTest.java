package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Company;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.ApplicationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest(JobController.class)
@ExtendWith(MockitoExtension.class)
class JobControllerIntegrationTest {

	private static final int APPLICATION_ID = 47;
	private static final int JOBOFFER_ID = 89;
	private static final int COMPANY_ID = 32;
	private static final String COMPANY_NAME = "test company name";
	private static final int USER_ID = 4;
	private static final String USER_USERNAME = "testuser";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ApplicationRepository applicationRepository;
	
	private User user;
	Applications application;

	@BeforeEach
	void beforeEach() {
		
		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);

		Company company = new Company();
		company.setId(COMPANY_ID);
		company.setUser(user);
		company.setName(COMPANY_NAME);

    	Joboffers joboffers = new Joboffers();
    	joboffers.setId(JOBOFFER_ID);
    	joboffers.setCompany(company);
    	
    	application = new Applications();
    	application.setId(APPLICATION_ID);
    	application.setUser(user);
    	application.setJoboffers(joboffers);
	}

	@Test
	@WithMockUser(USER_USERNAME)
	void testIndexView() throws Exception {

    	ArrayList<Applications> list = new ArrayList<Applications>();
    	list.add(application);
    	    	
    	when(applicationRepository.findByUserUsername(USER_USERNAME)).thenReturn(list);
		
		// 1. Verifying HTTP Request Matching
		MvcResult mvcResult = mockMvc.perform(get("/jobs")
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(JobController.JOBS_VIEW))
				.andExpect(model().attributeExists(JobController.AUTH_NAME))
				.andExpect(model().attributeExists(JobController.APPLICATION_LIST))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(COMPANY_NAME));
	}

    @Test
	@WithMockUser(USER_USERNAME)
    void testApplicationListView() throws Exception {
 
    	ArrayList<Applications> list = new ArrayList<Applications>();
    	list.add(application);
    	    	
    	when(applicationRepository.findByUserUsername(USER_USERNAME)).thenReturn(list);
		
		// 1. Verifying HTTP Request Matching
		MvcResult mvcResult = mockMvc.perform(get("/jobs/applications")
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(JobController.JOBS_APPLICATIONS_VIEW))
				.andExpect(model().attributeExists(JobController.AUTH_NAME))
				.andExpect(model().attributeExists(JobController.APPLICATION_LIST))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(COMPANY_NAME));
    }

    @Test
	@WithMockUser(USER_USERNAME)
    void testApplicationView() throws Exception {
     	    	
    	when(applicationRepository.findByUserUsernameAndId(USER_USERNAME, APPLICATION_ID)).thenReturn(Optional.of(application));
		
		// 1. Verifying HTTP Request Matching
		MvcResult mvcResult = mockMvc.perform(get("/jobs/applications/view/" + APPLICATION_ID)
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(JobController.JOBS_APPLICATIONS_VIEW_VIEW))
				.andExpect(model().attributeExists(JobController.AUTH_NAME))
				.andExpect(model().attributeExists(JobController.APPLICATION_DATA))
				.andExpect(model().attribute(JobController.APPLICATION_DATA, is(application)))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(COMPANY_NAME));
    }
}
