package ca.concordia.soen6011.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EducationUnitTest {

	public static final int EDUCATION_ID = 4;
	public static final String EDUCATION_DEGREE = "Software Engineering";
	public static final String EDUCATION_UNIVERSITY = "Concordia";
	public static final LocalDate EDUCATION_START = LocalDate.parse("2000-02-03");
	public static final LocalDate EDUCATION_FINISH = LocalDate.parse("2000-06-03");
	
    @Test
    public void testMembers() {

    	Resume r = new Resume();
    	
    	Education e = new Education();
    	
    	e.setId(EDUCATION_ID);
    	e.setDegree(EDUCATION_DEGREE);
    	e.setUniversity(EDUCATION_UNIVERSITY);
    	e.setStartDate(EDUCATION_START);
    	e.setFinishDate(EDUCATION_FINISH);
    	e.setResume(r);

        assertEquals(e.getId(), EDUCATION_ID);
        assertEquals(e.getDegree(), EDUCATION_DEGREE);
        assertEquals(e.getUniversity(), EDUCATION_UNIVERSITY);
        assertEquals(e.getStartDate(), EDUCATION_START);
        assertEquals(e.getFinishDate(), EDUCATION_FINISH);
        assertEquals(e.getResume(), r);
    }
}
