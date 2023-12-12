package ca.concordia.soen6011.repository;

import ca.concordia.soen6011.model.ResumeFile;

import java.util.Arrays;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase
public class ResumeFileRepositoryIntegrationTest {
	

	public static final int RESUMEFILE_ID = 1;
	public static final String RESUMEFILE_NAME = "Filename";
	public static final String RESUMEFILE_TYPE = "application/pdf";
	public static final byte[] RESUMEFILE_CONTENT = "Resume File Content".getBytes();

	@Autowired
	private DataSource dataSource;

    @Autowired
	private ResumeFileRepository resumeFileRepository;
	
	@Test
	void testInjectedComponentsAreNotNull(){

		assertNotNull(dataSource);
		assertNotNull(resumeFileRepository);
	}
	
	@Test
	@Sql("classpath:/testData.sql")
	void testSave(){

		Optional<ResumeFile> resumeFileResult = resumeFileRepository.findById(RESUMEFILE_ID);
		assertTrue(resumeFileResult.isPresent());
		
		ResumeFile resumeFile = resumeFileResult.get();
		assertNotNull(resumeFile);
		
		resumeFile.setName(RESUMEFILE_NAME);
		resumeFile.setType(RESUMEFILE_TYPE);
		resumeFile.setContent(RESUMEFILE_CONTENT);
		resumeFileRepository.save(resumeFile);

		Optional<ResumeFile> resumeFile2Result = resumeFileRepository.findById(RESUMEFILE_ID);
		assertTrue(resumeFile2Result.isPresent());

		ResumeFile resumeFile2 = resumeFile2Result.get();
		assertNotNull(resumeFile2);

		assertEquals(RESUMEFILE_NAME, resumeFile2.getName());
		assertEquals(RESUMEFILE_TYPE, resumeFile2.getType());
		assertTrue(Arrays.equals(RESUMEFILE_CONTENT, resumeFile2.getContent()));
	}
}