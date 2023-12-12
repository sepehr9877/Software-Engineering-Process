package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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
class SearchControllerUnitTest {

	private static final int JOBOFFER_ID = 21;
	private static final String JOBOFFER_TITLE = "test user";
	private static final int USER_ID = 47;
	private static final String USER_USERNAME = "testuser";
	private static final String SEARCH_TEXT = "Search Test";
	private static final String POST_COMMENT = "test comment";

	@Mock
	private JobOfferRepository jobOfferRepository; 
	
	@Mock
	private PostRepository postRepository; 
	
	@Mock
	private UserServiceImpl userService;
	
	@InjectMocks
	private SearchController controller;

	@Mock
	private UserDetails userDetails;

	User user;
	
	@BeforeEach
	void beforeEach() {

		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);
	}
	
    @Test
    void testSearchResultView() {
    	
    	Joboffers jobOffer = new Joboffers();
    	jobOffer.setId(JOBOFFER_ID);
    	jobOffer.setTitle(JOBOFFER_TITLE);
    	
    	ArrayList<Joboffers> list = new ArrayList<Joboffers>();
    	list.add(jobOffer);

    	ArrayList<User> list2 = new ArrayList<User>();
    	list2.add(user);
    	
    	Post post = new Post();
    	post.setComment(POST_COMMENT);

    	ArrayList<Post> list3 = new ArrayList<Post>();
    	list3.add(post);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userService.findUserByName(SEARCH_TEXT)).thenReturn(CompletableFuture.completedFuture(list2));
    	when(jobOfferRepository.findByTitleContainingIgnoreCase(SEARCH_TEXT)).thenReturn(list);
    	when(postRepository.findByCommentContainingIgnoreCase(SEARCH_TEXT)).thenReturn(list3);
    	
    	assertDoesNotThrow(() -> {    	
    		ModelAndView result = controller.postSearchResultView(SEARCH_TEXT, userDetails).get();

    		assertEquals(SearchController.SEARCH_VIEW, result.getViewName());
    		assertEquals(SEARCH_TEXT, (String)result.getModel().get(SearchController.SEARCH_TEXT));
    		
    		@SuppressWarnings("unchecked")
    		ArrayList<Joboffers> testlist = (ArrayList<Joboffers>)result.getModel().get(SearchController.JOB_OFFER_LIST);
    		assertEquals(JOBOFFER_TITLE, testlist.get(0).getTitle());

    		@SuppressWarnings("unchecked")
    		ArrayList<User> testlist2 = (ArrayList<User>)result.getModel().get(SearchController.USER_LIST);
    		assertEquals(USER_USERNAME, testlist2.get(0).getUsername());

    		@SuppressWarnings("unchecked")
    		ArrayList<Post> testlist3 = (ArrayList<Post>)result.getModel().get(SearchController.POST_LIST);
    		assertEquals(POST_COMMENT, testlist3.get(0).getComment());
    	});
    }
}
