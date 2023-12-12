package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.repository.ApplicationRepository;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * This controller handles page requests for jobs
 */
@RequestMapping("/jobs")
@Controller
public class JobController {
	
	/** Tag to store authenticated name */
	public static final String AUTH_NAME = "authname";
	/** Tag to store application list */
	public static final String APPLICATION_LIST = "applicationList";
	/** Tag to store application data */
	public static final String APPLICATION_DATA = "applicationData";
	
	/** Error view */
	public static final String ERROR_VIEW = "error";
	/** Jobs view */
	public static final String JOBS_VIEW = "jobs";
	/** Applications view */
	public static final String JOBS_APPLICATIONS_VIEW = "jobs_applications";
	/** View an application */
	public static final String JOBS_APPLICATIONS_VIEW_VIEW = "jobs_applications_view";
	
    private final ApplicationRepository applicationRepository;
	
	   
	/**
	 * Create a JobController with the repositories
	 * @param applicationRepository repository to store applications
	 */
	public JobController(ApplicationRepository applicationRepository) {

		this.applicationRepository = applicationRepository;
	}

	/**
	 * Displays a view with a list of applications and jobs
	 * @param userDetails authenitcated user
	 * @return the application/job view
	 */
	@GetMapping
	public CompletableFuture<ModelAndView> getIndexView(			
			@AuthenticationPrincipal UserDetails userDetails
			) {

		String username = userDetails.getUsername();
		
		return CompletableFuture.supplyAsync(() -> 

					applicationRepository.findByUserUsername(username)
				)
				.thenApply(al -> {

					HashMap<String, Object> hashMap = new HashMap<>();
					hashMap.put(AUTH_NAME, username);
					hashMap.put(APPLICATION_LIST, al);
 			        return hashMap;
				})
				.thenApply(map -> new ModelAndView(JOBS_VIEW, map));
	}

	/**
	 * Displays a view with a list of applications and jobs
	 * @param userDetails authenitcated user
	 * @return the application view
	 */
	@GetMapping("/applications")
	public CompletableFuture<ModelAndView> getApplicationListView(
			@AuthenticationPrincipal UserDetails userDetails
			) {

		String username = userDetails.getUsername();
		
		return CompletableFuture.supplyAsync(() -> 
		
				applicationRepository.findByUserUsername(username)
			)
			.thenApply(al -> {
	
				HashMap<String, Object> hashMap = new HashMap<>();
				hashMap.put(AUTH_NAME, username);
				hashMap.put(APPLICATION_LIST, al);
			    return hashMap;
			})
			.thenApply(map -> new ModelAndView(JOBS_APPLICATIONS_VIEW, map));
	}

	/**
	 * Displays the details of one application
	 * @param id id of application to show
	 * @param userDetails authenitcated user
	 * @return the application view
	 */
	@GetMapping("/applications/view/{id}")
	public CompletableFuture<ModelAndView> getApplicationView(
			@PathVariable("id")String id,
			@AuthenticationPrincipal UserDetails userDetails
			) {
		
		String username = userDetails.getUsername();

		return CompletableFuture.supplyAsync(() -> 
			
				applicationRepository.findByUserUsernameAndId(username, Integer.valueOf(id))
			)
			.thenApply(a -> {
	
				HashMap<String, Object> hashMap = new HashMap<>();
				hashMap.put(AUTH_NAME, username);
				if (a.isPresent()) {
					hashMap.put(APPLICATION_DATA, a.get());
				}
			    return hashMap;
			})
			.thenApply(map -> new ModelAndView(JOBS_APPLICATIONS_VIEW_VIEW, map));
	}	
}