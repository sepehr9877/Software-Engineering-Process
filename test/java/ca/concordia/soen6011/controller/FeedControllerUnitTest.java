package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.service.UserServiceImpl;

import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Optional;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class FeedControllerUnitTest {

	private static final String POST_COMMENT = "test comment";
	private static final String USER_USERNAME = "testuser";
	
	@Mock
	private UserDetails userDetails;

	@Mock
	private PostRepository postRepository; 
	
	@Mock
	private UserServiceImpl userService;

	@InjectMocks
	private FeedController controller;

	User user;
	
	@BeforeEach
	void beforeEach() {

		user = new User();
		user.setUsername(USER_USERNAME);
	}
	
    @Test
    void testIndexView() {
    	    	
    	Post post = new Post();
    	post.setComment(POST_COMMENT);

    	ArrayList<Post> list = new ArrayList<Post>();
    	list.add(post);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(postRepository.findAllByOrderByDateTimeAsc()).thenReturn(list);

    	assertDoesNotThrow(() -> {    	
    		ModelAndView result = controller.getIndexView(userDetails).get();

    		assertEquals(FeedController.FEED_VIEW, result.getViewName());
    		
    		@SuppressWarnings("unchecked")
    		ArrayList<Post> testlist = (ArrayList<Post>)result.getModel().get(FeedController.POST_LIST);
    		assertEquals(POST_COMMENT, testlist.get(0).getComment());    		
    	});
    }
	
    @Test
    void testCreatePost() {
    	    	
    	Post post = new Post();
    	post.setComment(POST_COMMENT);

    	ArrayList<Post> list = new ArrayList<Post>();
    	list.add(post);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userService.findUser(USER_USERNAME)).thenReturn(CompletableFuture.completedFuture(Optional.of(user)));
    	when(postRepository.findAllByOrderByDateTimeAsc()).thenReturn(list);

    	assertDoesNotThrow(() -> {    	
    		ModelAndView result = controller.postCreatePost(post, userDetails).get();

            verify(postRepository, times(1)).save(any(Post.class));
    		
    		assertEquals(FeedController.FEED_VIEW, result.getViewName());
    		
    		@SuppressWarnings("unchecked")
    		ArrayList<Post> testlist = (ArrayList<Post>)result.getModel().get(FeedController.POST_LIST);
    		assertEquals(POST_COMMENT, testlist.get(0).getComment());    		
    	});
    }

    @Test
    void testDefaultPost() {
    	Post p = controller.defaultPost();
    	assertNotNull(p);
    }	
}
