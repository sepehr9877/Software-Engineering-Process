package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.ApplicationRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class JobControllerUnitTest {

	public static final int JOBOFFER_ID = 89;
	public static final String JOBOFFER_COMPANY_NAME = "test company name";

	public static final int USER_ID = 4;
	public static final String USER_USERNAME = "testuser";

	public static final int APPLICATION_ID = 47;
	
	@Mock
	private ApplicationRepository applicationRepository; 
	
	@Mock
	private UserDetails userDetails;
		
	@InjectMocks
	private JobController controller;
	
	private User user;
	private Applications application;

	@BeforeEach
	void beforeEach() {
		
		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);

    	Joboffers joboffers = new Joboffers();
    	joboffers.setId(JOBOFFER_ID);
    	joboffers.setCompanyName(JOBOFFER_COMPANY_NAME);

    	application = new Applications();
    	application.setId(APPLICATION_ID);
    	application.setUser(user);
    	application.setJoboffers(joboffers);
	}
	
    @Test
    void testIndexView() {
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);

    	ArrayList<Applications> list = new ArrayList<Applications>();
    	list.add(application);
    	    	
    	when(applicationRepository.findByUserUsername(USER_USERNAME)).thenReturn(list);
    	
    	assertDoesNotThrow(() -> {    	
    	
    		ModelAndView result = controller.getIndexView(userDetails).get();
    		assertEquals(JobController.JOBS_VIEW, result.getViewName());
    		//assertEquals(list, (ArrayList<Applications>)result.getModel().get(JobController.APPLICATION_LIST));
    	});
    }

    @Test
    void testIndexViewWithNoUser() {
    	
    	when(userDetails.getUsername()).thenReturn("");

    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getIndexView(userDetails).get();
	        assertEquals(JobController.JOBS_VIEW, result.getViewName());
		});
    }

    @Test
    void testApplicationListView() {

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	
    	ArrayList<Applications> list = new ArrayList<Applications>();
    	list.add(application);
    	    	
    	when(applicationRepository.findByUserUsername(USER_USERNAME)).thenReturn(list);
    	
    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getApplicationListView(userDetails).get();
	        assertEquals(JobController.JOBS_APPLICATIONS_VIEW, result.getViewName());
    		//assertEquals(list, (ArrayList<Applications>)result.getModel().get(JobController.APPLICATION_LIST));
    	});

    }

    @Test
    void testApplicationListViewWithNoUser() {

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	
    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getApplicationListView(userDetails).get();
	        assertEquals(JobController.JOBS_APPLICATIONS_VIEW, result.getViewName());
    	});

    }

    @Test
    void testApplicationView() {

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(applicationRepository.findByUserUsernameAndId(USER_USERNAME, APPLICATION_ID)).thenReturn(Optional.of(application));
    	
    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getApplicationView(String.valueOf(APPLICATION_ID), userDetails).get();
	        assertEquals(JobController.JOBS_APPLICATIONS_VIEW_VIEW, result.getViewName());
    		assertEquals(application, (Applications)result.getModel().get(JobController.APPLICATION_DATA));
    	});

    }

    @Test
    void testApplicationViewWithNoUser() {

    	when(userDetails.getUsername()).thenReturn("");
    	when(applicationRepository.findByUserUsernameAndId("", APPLICATION_ID)).thenReturn(Optional.of(application));
    	
    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getApplicationView(String.valueOf(APPLICATION_ID), userDetails).get();
	        assertEquals(JobController.JOBS_APPLICATIONS_VIEW_VIEW, result.getViewName());
    	});

    }

    @Test
    void testApplicationViewWithNoApplication() {

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(applicationRepository.findByUserUsernameAndId(USER_USERNAME, APPLICATION_ID)).thenReturn(Optional.empty());
    	
    	assertDoesNotThrow(() -> {    	

    		ModelAndView result = controller.getApplicationView(String.valueOf(APPLICATION_ID), userDetails).get();
	        assertEquals(JobController.JOBS_APPLICATIONS_VIEW_VIEW, result.getViewName());
    	});
    }
}
