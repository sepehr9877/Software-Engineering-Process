package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.service.UserService;

import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * This controller handles search requests for jobs, users, applications and posts
 */
@RequestMapping("/search")
@Controller
public class SearchController {
	
	/** Tag to store authenticated name */
	public static final String AUTH_NAME = "authname";
	/** Tag to store job offer list */
	public static final String JOB_OFFER_LIST = "jobofferList";
	/** Tag to store post text */
	public static final String POST_LIST = "postList";
	/** Tag to store user list */
	public static final String USER_LIST = "userList";
	/** Input search text */
	public static final String SEARCH_TEXT = "searchtext";
	
	/** Error view */
	public static final String ERROR_VIEW = "error";
	/** Search view */
	public static final String SEARCH_VIEW = "search_results";
	
	private final JobOfferRepository jobOfferRepository;
    private final PostRepository postRepository;
	private final UserService userService;
	   
	/**
	 * Create a JobController with the repositories
	 * @param jobOfferRepository repository to store job offers
	 * @param jobOfferRepository repository to store job offers
	 * @param userService service to fetch user
	 */
	public SearchController(JobOfferRepository jobOfferRepository,
						    PostRepository postRepository,
						    UserService userService) {

		this.jobOfferRepository = jobOfferRepository;
		this.postRepository = postRepository;
		this.userService = userService;
	}

	/**
	 * Displays a view with a list of search results
	 * @param searchtext the search text
	 * @param userDetails the authenticated user
	 * @return the application/job view
	 */
	@PostMapping
	public CompletableFuture<ModelAndView> postSearchResultView(
			@RequestParam("searchtext") String searchtext,
			@AuthenticationPrincipal UserDetails userDetails) {

		String username = userDetails.getUsername();
		
		CompletableFuture<List<Joboffers>> jobofferFuture = CompletableFuture.supplyAsync(() ->  
			
			jobOfferRepository.findByTitleContainingIgnoreCase(searchtext)
		);	
		
		return userService.findUserByName(searchtext)
				.thenCombine(jobofferFuture, (ul, jo) -> {

					HashMap<String, Object> hashMap = new HashMap<>();
					hashMap.put(AUTH_NAME, username);
					hashMap.put(JOB_OFFER_LIST, jo);
					hashMap.put(SEARCH_TEXT, searchtext);
					hashMap.put(USER_LIST, ul);
 			        return hashMap;
				})
				.thenCombine(CompletableFuture.supplyAsync(() -> 
					
						postRepository.findByCommentContainingIgnoreCase(searchtext)
					) , (map, pl) -> {
							
					map.put(POST_LIST, pl);
					return map;
				})				
				.thenApply(map -> new ModelAndView(SEARCH_VIEW, map));
	}
}
