package ca.concordia.soen6011.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;



/** 
 * This is a User of the application that can login and access resumes and job offers 
 */
@Table(name = "\"user\"")
@Entity()
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="user_type",discriminatorType = DiscriminatorType.STRING)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Company company;
		
	@NotNull
	@Column(unique = true)
	private String username;
	@Column
	private String firstname;
	@Column
	private String lastname;
	@Email(message =  "It should be email format")
	@NotNull(message="email is null or misspelled")
	private String email;
	@Column
	@NotNull(message="password is null")
	private String password;

	@Column(columnDefinition = "boolean default true")
	private boolean isActive = true;

	@OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
	private Resume resume;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Role> roles;

	@OneToMany(mappedBy="user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Post> posts;

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
	public Company getCompany() {
		return company;
	}
	/** 
	 * Set the Company
	 * @param company the company
	 */
	public void setCompany(Company company) {
		this.company = company;
	}
	/** 
	 * Get the Username
	 * @return the username 
	 */
	public String getUsername() {
		return username;
	}
	/** 
	 * Set the Username
	 * @param username the username 
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/** 
	 * Get the Firstname
	 * @return the firstname 
	 */
	public String getFirstname() {
		return firstname;
	}
	/** 
	 * Set the Firstname
	 * @param firstname the firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	/** 
	 * Get the Lastname
	 * @return the lastname 
	 */
	public String getLastname() {
		return lastname;
	}
	/** 
	 * Set the Lastname
	 * @param lastname the lastname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	/** 
	 * Get the Email
	 * @return the email 
	 */
	public String getEmail() {
		return email;
	}
	/** 
	 * Set the Email
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** 
	 * Get the Password
	 * @return the password 
	 */
	public String getPassword() {
		return password;
	}
	/** 
	 * Set the Password
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/** 
	 * Get the Company
	 * @return the company 
	 */
	public String getRecentCompany() {
		return null;
	}
	/** 
	 * Get the IsAdmin
	 * @return the flag isAdmin 
	 */
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	/** 
	 * Get the Roles
	 * @return the list of roles 
	 */
	public List<Role> getRoles() {
		return roles;
	}
	/** 
	 * Set the Roles
	 * @param authorities the list of roles 
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	/** 
	 * Get the Resume
	 * @return the resume */
	public Resume getResume() {
		return resume;
	}
	/** 
	 * Set the Resume
	 * @param resume the list of resume 
	 */
	public void setResume(Resume resume) {
		this.resume = resume;
	}
	/** 
	 * Get the Posts
	 * @return the list of posts 
	 */
	public List<Post> getPosts() {
		return posts;
	}
	/** 
	 * Set the Posts
	 * @param posts the list of posts */
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	/** 
	 * Get the list of roles
	 * @return the list of roles 
	 */
	public String[] getRoleArray() {
		List<String> list = this.roles.stream().map(Role::getName).toList();
		return list.toArray(new String[list.size()]);
	}	
}
