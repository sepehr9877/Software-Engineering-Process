package ca.concordia.soen6011.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Lob;

/**
 * A Resume File is a resume stored in a file.
 */
@Entity
public class ResumeFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

    @Lob
    @Column( length = 1000000 )
    private byte[] content;
    
    private String name;
    private String type;
    
	@OneToOne
	@JoinColumn(name = "resume_id")
	private Resume resume;

	/**
	 * Default constructor
	 */
	public ResumeFile() {
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
	 * Get the content
	 * @return the content 
	 */
	public byte[] getContent() {
		return content;
	}
	/** 
	 * Set the content
	 * @param content the content 
	 */
	public void setContent(byte[] content) {
		this.content = content;
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
	 * Get the Type
	 * @return the type 
	 */
	public String getType() {
		return type;
	}
	/** 
	 * Set the Type
	 * @param type the type 
	 */
	public void setType(String type) {
		this.type = type;
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
