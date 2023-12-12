package ca.concordia.soen6011.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ResumeUnitTest {

	public static final int RESUME_ID = 4;
	public static final LocalDate RESUME_CREATED = LocalDate.parse("2020-02-03");
	
    @Test
    public void testMembers() {

    	User c = new User();
    	
    	Resume r = new Resume();
    	
    	r.setId(RESUME_ID);
    	r.setCreatedDate(RESUME_CREATED);
    	r.setUser(c);

        assertEquals(r.getId(), RESUME_ID);
        assertEquals(r.getCreatedDate(), RESUME_CREATED);
        assertEquals(r.getUser(), c);
    }
}
