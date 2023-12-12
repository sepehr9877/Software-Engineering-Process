package ca.concordia.soen6011.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SkillUnitTest {

	public static final int SKILL_ID = 4;
	public static final String SKILL_NAME = "Juggling";
	public static final String SKILL_DESCRIPTION = "I can juggle three balls";
	
    @Test
    public void testMembers() {

    	Resume r = new Resume();
    	
    	Skill s = new Skill();
    	
    	s.setId(SKILL_ID);
    	s.setName(SKILL_NAME);
    	s.setDescription(SKILL_DESCRIPTION);
    	s.setResume(r);

        assertEquals(s.getId(), SKILL_ID);
        assertEquals(s.getName(), SKILL_NAME);
        assertEquals(s.getDescription(), SKILL_DESCRIPTION);
        assertEquals(s.getResume(), r);
    }
}
