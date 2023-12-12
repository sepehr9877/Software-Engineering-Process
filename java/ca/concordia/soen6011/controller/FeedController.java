package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.service.UserService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * This controller handles creating and displaying posts
 */
@RequestMapping("/feed")
@Controller
public class FeedController {

	/** Tag to store authenticated name */
	public static final String AUTH_NAME = "authname";
	/** Tag to store post list */
	public static final String POST_LIST = "postList";
	/** Tag to store bool allow post */
	public static final String ALLOW_POST = "allowPost";

	/** Feed view */
	public static final String FEED_VIEW = "feed";
	/** Redirect to home */
	public static final String REDIRECT_FEED_VIEW = "redirect:/feed";
	/** Error view */
	public static final String ERROR_VIEW = "error";

	private final PostRepository postRepository;
	private final UserService userService;

	/**
	 * Create a FeedController with the repositories
	 * @param postRepository postRepository to store posts
	 * @param userService service to find users
	 */
	public FeedController(PostRepository postRepository,
			UserService userService) {

		this.postRepository = postRepository;
		this.userService = userService;
	}

	/**
	 * Displays a view with a list of search results
	 * @param userDetails authentication userDetails
	 * @return the application/job view
	 */
	@GetMapping
	public CompletableFuture<ModelAndView> getIndexView(
			@AuthenticationPrincipal UserDetails userDetails) {

		String username = userDetails.getUsername();
		
		return CompletableFuture.supplyAsync(

				postRepository::findAllByOrderByDateTimeAsc
			)
			.thenApply(pl -> 

				createMap(username, pl)
			)
			.thenApply(map -> new ModelAndView(FEED_VIEW, map));
	}


	/**
	 * Displays a view with a list of search results
	 * @param post post to create
	 * @param userDetails authentication userDetails
	 * @return the application/job view
	 */
	@PostMapping
	public CompletableFuture<ModelAndView> postCreatePost(
			@ModelAttribute("post") Post post,
			@AuthenticationPrincipal UserDetails userDetails) {

		String username = userDetails.getUsername();
				
		Optional<User> userOptional = userService.findUser(username).join();
		
		if (userOptional.isEmpty()) {
			return CompletableFuture.completedFuture(new ModelAndView(ERROR_VIEW));
		}
		User user = userOptional.get();
		
		return CompletableFuture.supplyAsync(() -> {

				post.setDateTime(LocalDateTime.now());
				post.setUser(user);
				postRepository.save(post);
				
				return postRepository.findAllByOrderByDateTimeAsc();
			})
			.thenApply(pl -> 
				
				createMap(username, pl)
			)
			.thenApply(map -> new ModelAndView(FEED_VIEW, map));
	}

	
	/**
	 * Create a new empty Skill to edit
	 * @return the new skill
	 */
	@ModelAttribute(value = "newpost")
	public Post defaultPost()
	{
	    return new Post();
	}
	
	private HashMap<String, Object> createMap(String username, List<Post> postList) {
		
		HashMap<String, Object> hashMap = new HashMap<>();
    	hashMap.put(AUTH_NAME, username);
    	hashMap.put(POST_LIST, postList);
    	hashMap.put(ALLOW_POST, true);
    	return hashMap;
	}
}