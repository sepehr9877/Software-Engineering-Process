package ca.concordia.soen6011.model;

import java.time.LocalDate;
import jakarta.persistence.*;



@Entity
public class Applications {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	public Applications() {
		
	}
	public enum ApplicationStatus {

		ACCEPTED("Accepted"),
		INPROGRESS("In Progress"),
		DECLINED("Declined");
		
	    public final String label;

	    private ApplicationStatus(String label) {
	        this.label = label;
	    }
	    
	    @Override 
	    public String toString() { 
	        return this.label; 
	    }	    
	}
	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="joboffer_id")
	private Joboffers joboffers;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="interview_id")
	private Interview interview;
	private LocalDate dateapplied;
	public Applications(ApplicationStatus status,
			User user,
			Joboffers joboffers,
			LocalDate dateapplied) {
		this.status = status;
		this.user = user;
		this.joboffers = joboffers;
		this.dateapplied=dateapplied;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Joboffers getJoboffers() {
		return joboffers;
	}
	public void setJoboffers(Joboffers joboffers) {
		this.joboffers = joboffers;
	}

	public LocalDate getDateapplied() {
		return dateapplied;
	}

	public void setDateapplied(LocalDate dateapplied) {
		this.dateapplied = dateapplied;
	}

	public Interview getInterview() {
		return interview;
	}

	public void setInterview(Interview interview) {
		this.interview = interview;
	}
}
