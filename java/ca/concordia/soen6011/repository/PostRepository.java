package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.Post;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


/** 
 * Users have Posts to share news. They are saved in the repository.
 */
public interface PostRepository extends JpaRepository<Post,Integer> {

    /**
     * Get all posts and return by date ascending
     * @return a list of posts
     */
	List<Post> findAllByOrderByDateTimeAsc();

    /**
     * Get all posts that contain the text
     * @param text text to search for
     * @return a list of posts
     */
	public List<Post> findByCommentContainingIgnoreCase(String text);
}
