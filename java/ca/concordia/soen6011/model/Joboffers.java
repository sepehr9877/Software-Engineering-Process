package ca.concordia.soen6011.model;

import java.time.LocalDate;

import jakarta.persistence.*;


@Entity
public class Joboffers {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	@Column(length = 500)
	private String description;
	private int staff;
	private String companyName;
	private LocalDate published;
	
	public Joboffers() {
		
	}
	public enum JobOfferStatus {

		ACCEPTED,REJECTED,PENDING
	}
	@JoinColumn(name = "company_id")
	@ManyToOne
	private Company company;
	private int lowsalery;
	private int highsalery;
	private LocalDate deadline;
	@Enumerated(EnumType.STRING)
	private JobOfferStatus status;
	public Joboffers(String title, String description, int staff, String companyName, LocalDate published, Company company,
			int highsalery,int lowsalery, LocalDate deadline, JobOfferStatus status) {
		
		this.title = title;
		this.description = description;
		this.staff = staff;
		this.companyName = companyName;
		this.published = published;
		this.company = company;
		this.lowsalery = lowsalery;
		this.highsalery=highsalery;
		this.deadline = deadline;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStaff() {
		return staff;
	}
	public void setStaff(int staff) {
		this.staff = staff;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public LocalDate getPublished() {
		return published;
	}
	public void setPublished(LocalDate published) {
		this.published = published;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	public LocalDate getDeadline() {
		return deadline;
	}
	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}
	public JobOfferStatus getStatus() {
		return status;
	}
	public void setStatus(JobOfferStatus status) {
		this.status = status;
	}
	public int getLowsalery() {
		return lowsalery;
	}
	public void setLowsalery(int lowsalery) {
		this.lowsalery = lowsalery;
	}
	public int getHighsalery() {
		return highsalery;
	}
	public void setHighsalery(int highsalery) {
		this.highsalery = highsalery;
	}
	
}
