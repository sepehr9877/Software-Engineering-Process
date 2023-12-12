package ca.concordia.soen6011.model;

import jakarta.persistence.*;

/** 
 * The user has authority that control what pages they can access 
 */
@Entity()
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JoinColumn(name = "user_id")
	@ManyToOne
	private User user;
	
	@Column
	private String name;

	
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
	/** 
	 * Get the Name
	 * @return the name 
	 */
	public String getName() {
		return name;
	}
	/** 
	 * Set the Name
	 * @param name the name 
	 */
	public void setName(String name) {
		this.name = name;
	}

}
