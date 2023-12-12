package ca.concordia.soen6011.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class RoleUnitTest {

	public static final int ROLE_ID = 4;
	public static final String ROLE_NAME = "test";
	
    @Test
    public void testMembers() {

    	User u = new User();
    	
    	Role a = new Role();
    	
    	a.setId(ROLE_ID);
    	a.setName(ROLE_NAME);
    	a.setUser(u);

        assertEquals(ROLE_ID, a.getId());
        assertEquals(ROLE_NAME, a.getName());
        assertEquals(u, a.getUser());
    }
}
