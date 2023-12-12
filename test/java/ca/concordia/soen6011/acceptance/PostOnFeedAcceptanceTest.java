package ca.concordia.soen6011.acceptance;

import ca.concordia.soen6011.controller.FeedController;
import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.model.Post.PostType;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.PostRepository;
import ca.concordia.soen6011.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class PostOnFeedAcceptanceTest {

	private static final String USER_USERNAME = "TheTestMaster";
	private static final String POST_COMMENT = "test comment";
	private static final PostType POST_TYPE = PostType.TEXT;
	private static final LocalDateTime POST_DATE_TIME = LocalDateTime.of(1, 2, 3, 4, 5, 6);

	@Autowired
	private MockMvc mockMvc;

    @Autowired
	private PostRepository postRepository;

    @Autowired
	private UserRepository userRepository;

    private User user;
    private Post post;
    
	@BeforeEach
    void beforeEach() {

    	user = new User();
    	user.setUsername(USER_USERNAME);
    	
    	post = new Post();
    	
		post.setComment(POST_COMMENT);
		post.setType(POST_TYPE);
    	post.setDateTime(POST_DATE_TIME);
    	post.setUser(user);
    	
    	ArrayList<Post> list = new ArrayList<Post>();
    	list.add(post);
    	user.setPosts(list);

        try{
        	userRepository.save(user);
        }
        catch (Exception e){}
	}
	
	@AfterEach
    public void afterEach() {

		try {
			postRepository.deleteById(post.getId());
			userRepository.deleteById(user.getId());
		}
		catch (Exception e) {}
	}

	
    @Test
    @WithMockUser(USER_USERNAME)
    void testCreatePost() throws Exception {

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

		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(POST_COMMENT));
}
    
	
    @Test
    @WithMockUser(USER_USERNAME)
    void testViewFeed() throws Exception {
 
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

		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(POST_COMMENT));
    }
}
