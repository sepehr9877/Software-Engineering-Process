package ca.concordia.soen6011.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * A Skill is additional information that represent soft and hard skills the Candidate has gained
 */
@Entity
public class Skill {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JoinColumn(name = "resume_id")
	@ManyToOne
	private Resume resume;

	private Boolean isRemoved = false;
	private String name;
	private String description;
	
	/**
	 * Default constructor
	 */
	public Skill() {
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
	 * Get the IsRemoved
	 * @return if the skill is removed 
	 */
	public Boolean getIsRemoved() {
		return isRemoved;
	}
	/** 
	 * Set the IsRemoved
	 * @param isRemoved the skill has been removed 
	 */
	public void setIsRemoved(Boolean isRemoved) {
		this.isRemoved = isRemoved;
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
	 * Get the Description
	 * @return the description 
	 */
	public String getDescription() {
		return description;
	}
	/** 
	 * Set the Description
	 * @param description the description 
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/** 
	 * Get the Resume
	 * @return the resume 
	 */
	public Resume getResume() {
		return resume;
	}
	/** 
	 * Set the Resume
	 * @param resume the resume 
	 */
	public void setResume(Resume resume) {
		this.resume = resume;
	}

}
