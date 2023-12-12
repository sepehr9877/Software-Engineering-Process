package ca.concordia.soen6011.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * A Post is a news update that a user wants to share
 */
@Entity
public class Post {
	
	public enum PostType {
		TEXT,
		LINK
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;

	private Boolean isHidden = false;
	private String comment;
	private String content;
	@Enumerated(EnumType.STRING)
	private PostType type;
	private LocalDateTime dateTime;
	
	/**
	 * Default constructor
	 */
	public Post() {
		// empty
	}
	
	/** 
	 * Get the Id
	 * @return the id 
	 */
	public int getId() {
		return id;
	}
	/** 
	 * Set the Id
	 * @param id the id 
	 */
	public void setId(int id) {
		this.id = id;
	}
	/** 
	 * Get the Comment
	 * @return the comment 
	 */
	public String getComment() {
		return comment;
	}
	/** 
	 * Set the Comment
	 * @param content the comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/** 
	 * Get the Content
	 * @return the content 
	 */
	public String getContent() {
		return content;
	}
	/** 
	 * Set the Content
	 * @param content the content
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/** 
	 * Get the Type
	 * @return the type 
	 */
	public PostType getType() {
		return type;
	}
	/** 
	 * Set the Type
	 * @param type the type 
	 */
	public void setType(PostType type) {
		this.type = type;
	}
	/** 
	 * Get the LocalDateTime
	 * @return the date and time 
	 */
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	/** 
	 * Set the LocalDateTime
	 * @param dateTime the date and time 
	 */
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	/** 
	 * Get the IsHidden
	 * @return if the post is hidden 
	 */
	public Boolean getIsHidden() {
		return isHidden;
	}
	/** 
	 * Set the IsHidden
	 * @param isHidden the post has been hidden 
	 */
	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	/** 
	 * Get the User
	 * @return the user 
	 */
	public User getUser() {
		return user;
	}
	/** 
	 * Set the User
	 * @param user the user 
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
