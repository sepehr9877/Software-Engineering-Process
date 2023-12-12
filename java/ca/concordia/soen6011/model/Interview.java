package ca.concordia.soen6011.model;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Interview {
	
	public enum InterviewType {

		ONLINE("Online"),
		IN_PERSON("In Person");
		
	    public final String label;

	    private InterviewType(String label) {
	        this.label = label;
	    }
	    
	    @Override 
	    public String toString() { 
	        return this.label; 
	    }	    
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private LocalDate date;
	
	private LocalTime time;
	@Enumerated(EnumType.STRING)
	private InterviewType type;
	public Interview() {
	}

	


	public Interview(LocalDate date, LocalTime time, InterviewType type) {
		
		this.date = date;
		this.time = time;
		this.type = type;
	}




	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public InterviewType getType() {
		return type;
	}

	public void setType(InterviewType type) {
		this.type = type;
	}


}
