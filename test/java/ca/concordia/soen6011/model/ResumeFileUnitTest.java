package ca.concordia.soen6011.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ResumeFileUnitTest {

	public static final int RESUMEFILE_ID = 4;
	public static final String RESUMEFILE_NAME = "Resume File Name";
	public static final String RESUMEFILE_TYPE = "Resume File Type";
	public static final byte[] RESUMEFILE_CONTENT = "Resume File Content".getBytes();
	
    @Test
    public void testMembers() {

    	Resume resume = new Resume();
    	
    	ResumeFile resumeFile = new ResumeFile();
    	
    	resumeFile.setId(RESUMEFILE_ID);
    	resumeFile.setName(RESUMEFILE_NAME);
    	resumeFile.setType(RESUMEFILE_TYPE);
    	resumeFile.setContent(RESUMEFILE_CONTENT);
    	resumeFile.setResume(resume);

        assertEquals(RESUMEFILE_ID, resumeFile.getId());
        assertEquals(RESUMEFILE_NAME, resumeFile.getName());
        assertEquals(RESUMEFILE_TYPE, resumeFile.getType());
        assertEquals(RESUMEFILE_CONTENT, resumeFile.getContent());
        assertEquals(resume, resumeFile.getResume());
    }
}
