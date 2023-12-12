package ca.concordia.soen6011.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;

/** 
 * A user creates a Company to write job offers and accepts applications 
 */
@Entity
public class Company {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JoinColumn(name = "user_id")
	@OneToOne
	private User user;
	
	@Column
	private String name;

	public Company(User user, String name) {
		
		this.user = user;
		this.name = name;
	}
	public Company() {
		super();
	}
	@OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
	private List<Joboffers> joboffers;

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
	/** 
	 * Get the Joboffers
	 * @return the joboffers 
	 */
	public List<Joboffers> getJoboffers() {
		return joboffers;
	}
	/** 
	 * Get the Joboffers
	 * @param joboffers the joboffers 
	 */
	public void setJoboffers(List<Joboffers> joboffers) {
		this.joboffers = joboffers;
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
