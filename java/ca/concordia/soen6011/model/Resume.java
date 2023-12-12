package ca.concordia.soen6011.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;

/**
 * A Resume is created by a user to apply for a job. It has sections Education, Experience and Skills.
 */
@Entity
public class Resume {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(mappedBy = "resume",cascade = CascadeType.ALL)
	private ResumeFile resumeFile;

	@OneToMany(mappedBy = "resume",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Skill> skills;
	
	@OneToMany(mappedBy = "resume",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Education> education;
	
	@OneToMany(mappedBy = "resume",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Experience> experience;

	private LocalDate createdDate;

	/**
	 * Default constructor
	 */
	public Resume() {
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
	 * Get the user
	 * @return the user 
	 */
	public User getUser() {
		return user;
	}
	/** 
	 * Set the user
	 * @param user the user 
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/** 
	 * Get the Skills
	 * @return the list of skills 
	 */
	public List<Skill> getSkills() {
		return skills;
	}
	/** 
	 * Set the Skills
	 * @param skills the list of skills 
	 */
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	/** 
	 * Get the Education
	 * @return the list of education 
	 */
	public List<Education> getEducation() {
		return education;
	}
	/** 
	 * Set the Education
	 * @param education the education 
	 */
	public void setEducation(List<Education> education) {
		this.education = education;
	}
	/** 
	 * Get the Experience
	 * @return the list of experience 
	 */
	public List<Experience> getExperience() {
		return experience;
	}
	/** 
	 * Set the Experience
	 * @param experience the experience 
	 */
	public void setExperience(List<Experience> experience) {
		this.experience = experience;
	}
	/** 
	 * Get the CreatedDate
	 * @return the createdDate 
	 */
	public LocalDate getCreatedDate() {
		return createdDate;
	}
	/** 
	 * Set the CreatedDate
	 * @param createdDate the createdDate 
	 */
	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}
	/** 
	 * Get the ResumeFile
	 * @return the resumeFile 
	 */
	public ResumeFile getResumeFile() {
		return resumeFile;
	}
	/** 
	 * Set the ResumeFile
	 * @param resumeFile the resumeFile 
	 */
	public void setResumeFile(ResumeFile resumeFile) {
		this.resumeFile = resumeFile;
	}
}