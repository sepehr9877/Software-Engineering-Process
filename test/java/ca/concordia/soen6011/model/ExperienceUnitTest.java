package ca.concordia.soen6011.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ExperienceUnitTest {

	public static final int EXPERIENCE_ID = 4;
	public static final String EXPERIENCE_COMPANY = "Work Study";
	public static final String EXPERIENCE_DESCRIPTION = "I worked in C++";
	public static final LocalDate EXPERIENCE_START = LocalDate.parse("2020-02-03");
	public static final LocalDate EXPERIENCE_FINISH = LocalDate.parse("2023-06-03");
	
    @Test
    public void testMembers() {

    	Resume r = new Resume();
    	
    	Experience e = new Experience();
    	
    	e.setId(EXPERIENCE_ID);
    	e.setCompany(EXPERIENCE_COMPANY);
    	e.setDescription(EXPERIENCE_DESCRIPTION);
    	e.setStartDate(EXPERIENCE_START);
    	e.setFinishDate(EXPERIENCE_FINISH);
    	e.setResume(r);

        assertEquals(e.getId(), EXPERIENCE_ID);
        assertEquals(e.getCompany(), EXPERIENCE_COMPANY);
        assertEquals(e.getDescription(), EXPERIENCE_DESCRIPTION);
        assertEquals(e.getStartDate(), EXPERIENCE_START);
        assertEquals(e.getFinishDate(), EXPERIENCE_FINISH);
        assertEquals(e.getResume(), r);
    }
}
