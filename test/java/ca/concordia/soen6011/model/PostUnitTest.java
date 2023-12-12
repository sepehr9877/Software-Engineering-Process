package ca.concordia.soen6011.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import ca.concordia.soen6011.model.Post.PostType;

public class PostUnitTest {

	public static final int POST_ID = 4;
	private static final String POST_COMMENT = "Juggling";
	private static final String POST_CONTENT = "I can juggle three balls";
	private static final PostType POST_TYPE = PostType.TEXT;
	private static final LocalDateTime POST_DATE_TIME = LocalDateTime.of(1, 2, 3, 4, 5, 6);
	private static final boolean POST_IS_HIDDEN = true;
	
    @Test
    void testMembers() {

    	User u = new User();
    	
    	Post post = new Post();
    	
    	post.setId(POST_ID);
		post.setComment(POST_COMMENT);
		post.setContent(POST_CONTENT);
		post.setType(POST_TYPE);
    	post.setDateTime(POST_DATE_TIME);
    	post.setIsHidden(POST_IS_HIDDEN);
    	post.setUser(u);

		assertEquals(POST_COMMENT, post.getComment());
		assertEquals(POST_CONTENT, post.getContent());
        assertEquals(POST_TYPE, post.getType());
        assertEquals(POST_DATE_TIME, post.getDateTime());
        assertEquals(POST_IS_HIDDEN, post.getIsHidden());
        assertEquals(u, post.getUser());
    }
}
