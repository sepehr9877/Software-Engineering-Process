package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest(FeedController.class)
@ExtendWith(MockitoExtension.class)
class FeedControllerIntegrationTest {

	private static final String POST_COMMENT = "test comment";
	private static final String USER_USERNAME = "testuser";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private PostRepository postRepository;
	
	@MockBean
	private UserServiceImpl userService;

	private User user;
	
	@BeforeEach
	void beforeEach() {
		
		user = new User();
		user.setUsername(USER_USERNAME);
	}

	@Test
	@WithMockUser(USER_USERNAME)
	void testIndexView() throws Exception {

    	Post post = new Post();
    	post.setComment(POST_COMMENT);
    	post.setUser(user);

    	ArrayList<Post> list = new ArrayList<Post>();
    	list.add(post);
    	user.setPosts(list);

    	when(postRepository.findAllByOrderByDateTimeAsc()).thenReturn(list);

		// 1. Verifying HTTP Request Matching
    	MvcResult mvcResult = mockMvc.perform(get("/feed")
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(FeedController.FEED_VIEW))
				.andExpect(model().attributeExists(FeedController.AUTH_NAME))
				.andExpect(model().attributeExists(FeedController.POST_LIST))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(POST_COMMENT));
	}

	@Test
	@WithMockUser(USER_USERNAME)
	void testCreatePost() throws Exception {

    	Post post = new Post();
    	post.setComment(POST_COMMENT);
    	post.setUser(user);

    	ArrayList<Post> list = new ArrayList<Post>();
    	list.add(post);
    	user.setPosts(list);

    	when(userService.findUser(USER_USERNAME)).thenReturn(CompletableFuture.completedFuture(Optional.of(user)));
    	when(postRepository.findAllByOrderByDateTimeAsc()).thenReturn(list);

		// 1. Verifying HTTP Request Matching
    	MvcResult mvcResult = mockMvc.perform(post("/feed")
    			.flashAttr("post", post)
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(FeedController.FEED_VIEW))
				.andExpect(model().attributeExists(FeedController.AUTH_NAME))
				.andExpect(model().attributeExists(FeedController.POST_LIST))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(POST_COMMENT));
	}
}