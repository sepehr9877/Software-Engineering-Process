package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.repository.JobOfferRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest(SearchController.class)
@ExtendWith(MockitoExtension.class)
class SearchControllerIntegrationTest {

	private static final int JOBOFFER_ID = 21;
	private static final String JOBOFFER_TITLE = "test user";
	private static final int USER_ID = 4;
	private static final String USER_USERNAME = "TheTestMaster";
	private static final String USER_FIRSTNAME = "Testy";
	private static final String USER_LASTNAME = "McTesterface";
	private static final String SEARCH_TEXT = "Search Test";
	private static final String POST_COMMENT = "test comment";

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private JobOfferRepository jobOfferRepository;
	
	@MockBean
	private PostRepository postRepository;
	
	@MockBean
	private UserServiceImpl userService;
	
	private User user;
	
	@BeforeEach
	void beforeEach() {
		
		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);
		user.setFirstname(USER_FIRSTNAME);
		user.setLastname(USER_LASTNAME);
	}

	@Test
	@WithMockUser(USER_USERNAME)
	void testSearchResultView() throws Exception {

		Joboffers jobOffer = new Joboffers();
    	jobOffer.setId(JOBOFFER_ID);
    	jobOffer.setTitle(JOBOFFER_TITLE);
    	
    	ArrayList<Joboffers> list = new ArrayList<Joboffers>();
    	list.add(jobOffer);

    	ArrayList<User> list2 = new ArrayList<User>();
    	list2.add(user);
    	
    	Post post = new Post();
    	post.setComment(POST_COMMENT);
    	post.setUser(user);

    	ArrayList<Post> list3 = new ArrayList<Post>();
    	list3.add(post);
    	user.setPosts(list3);

    	when(userService.findUser(USER_USERNAME)).thenReturn(CompletableFuture.completedFuture(Optional.of(user)));
    	when(jobOfferRepository.findByTitleContainingIgnoreCase(SEARCH_TEXT)).thenReturn(list);
    	when(userService.findUserByName(SEARCH_TEXT)).thenReturn(CompletableFuture.completedFuture(list2));
    	when(postRepository.findByCommentContainingIgnoreCase(SEARCH_TEXT)).thenReturn(list3);

		// 1. Verifying HTTP Request Matching
    	MvcResult mvcResult = mockMvc.perform(post("/search")
				.param("searchtext", SEARCH_TEXT)
				.with(csrf()))
    			.andExpect(request().asyncStarted())
    			.andReturn();
    			
		mockMvc.perform(asyncDispatch(mvcResult))
				.andExpect(status().isOk())
				.andExpect(view().name(SearchController.SEARCH_VIEW))
				.andExpect(model().attributeExists(SearchController.AUTH_NAME))
				.andExpect(model().attributeExists(SearchController.USER_LIST))
				.andExpect(model().attributeExists(SearchController.SEARCH_TEXT))
				.andExpect(model().attributeExists(SearchController.JOB_OFFER_LIST))
				.andExpect(model().attributeExists(SearchController.POST_LIST))
				.andReturn();

		// 2. Verifying Input Deserialization
		
		// 3. Verifying Input Validation
		
		// 4. Verifying Business Logic Calls
		
		// 5. Verifying Output Serialization
		String actualResponseBody = mvcResult.getResponse().getContentAsString();
		assertTrue(actualResponseBody.contains(JOBOFFER_TITLE));
		assertTrue(actualResponseBody.contains(USER_FIRSTNAME));
	}
}