package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Post;
import ca.concordia.soen6011.model.Post.PostType;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase
class PostRepositoryIntegrationTest {
	
	private static final int POST_ID = 1;
	private static final String POST_COMMENT = "test comment";
	private static final String POST_CONTENT = "I can juggle three balls";
	private static final PostType POST_TYPE = PostType.TEXT;
	private static final LocalDateTime POST_DATE_TIME = LocalDateTime.of(1, 2, 3, 4, 5, 6);
	private static final boolean POST_IS_HIDDEN = true;

	@Autowired
	private DataSource dataSource;

    @Autowired
	private PostRepository postRepository;

	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(postRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<Post> postResult = postRepository.findById(POST_ID);
		assertTrue(postResult.isPresent());
		
		Post post = postResult.get();
		assertNotNull(post);

		User u = new User();
		
		post.setComment(POST_COMMENT);
		post.setContent(POST_CONTENT);
		post.setType(POST_TYPE);
    	post.setDateTime(POST_DATE_TIME);
    	post.setIsHidden(POST_IS_HIDDEN);
    	post.setUser(u);
		
		postRepository.save(post);

		Optional<Post> post2Result = postRepository.findById(POST_ID);
		assertTrue(post2Result.isPresent());

		Post post2 = post2Result.get();
		assertNotNull(post2);

		assertEquals(POST_COMMENT, post2.getComment());
		assertEquals(POST_CONTENT, post2.getContent());
        assertEquals(POST_TYPE, post2.getType());
        assertEquals(POST_DATE_TIME, post.getDateTime());
        assertEquals(POST_IS_HIDDEN, post2.getIsHidden());
        assertEquals(u, post2.getUser());
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testFindAllByOrderByDateTimeAsc(){
		
		List<Post> postList = postRepository.findAllByOrderByDateTimeAsc();
		assertEquals(1, postList.size());

		Post post = postList.get(0);
		assertEquals("a comment", post.getComment());
        assertEquals(POST_TYPE, post.getType());
	}

	@Test
	@Sql("classpath:/testData.sql")
	void testFindByCommentContainingIgnoreCase(){
		
		List<Post> postList = postRepository.findByCommentContainingIgnoreCase("a");
		assertEquals(1, postList.size());

		Post post = postList.get(0);
		assertEquals("a comment", post.getComment());
        assertEquals(POST_TYPE, post.getType());
	}
}