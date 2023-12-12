package ca.concordia.soen6011.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


/**
 * This represents the work experience that the Candidate has gained
 */
@Entity
public class Experience {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "resume_id")
	private Resume resume;

	private Boolean isRemoved = false;
	private String company;
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate finishDate;

	/**
	 * Default constructor
	 */
	public Experience() {
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
	/** 
	 * Get the Company
	 * @return the company 
	 */
	public String getCompany() {
		return company;
	}
	/** 
	 * Set the Company
	 * @param company the company 
	 */
	public void setCompany(String company) {
		this.company = company;
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
	 * Get the StartDate
	 * @return the startDate 
	 */
	public LocalDate getStartDate() {
		return startDate;
	}
	/** 
	 * Set the StartDate
	 * @param startDate the startDate 
	 */
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	/** 
	 * Get the FinishDate
	 * @return the finishDate 
	 */
	public LocalDate getFinishDate() {
		return finishDate;
	}
	/** 
	 * Set the FinishDate
	 * @param finishDate the finishDate 
	 */
	public void setFinishDate(LocalDate finishDate) {
		this.finishDate = finishDate;
	}

}
