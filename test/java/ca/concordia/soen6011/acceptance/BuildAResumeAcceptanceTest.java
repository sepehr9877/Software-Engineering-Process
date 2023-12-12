package ca.concordia.soen6011.acceptance;

import ca.concordia.soen6011.controller.ProfileController;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Education;
import ca.concordia.soen6011.model.Experience;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.ResumeFile;
import ca.concordia.soen6011.model.Skill;
import ca.concordia.soen6011.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class BuildAResumeAcceptanceTest {

	private static final String USER_USERNAME = "testuser";
	private static final String USER_EMAIL = "Testy@gmail.com";
	private static final String USER_PASSWORD = "12345";
	private static final String USER_FIRSTNAME = "Testy";
	private static final String USER_LASTNAME = "McTesterface";

	private static final String RESUME_FILE_NAME = "Resume File Name";
	private static final String RESUME_FILE_TYPE = "Resume File Type";
	private static final byte[] RESUME_FILE_CONTENT = "Resume File Content".getBytes();

	@Autowired
	private MockMvc mockMvc;

    @Autowired
	private UserRepository userRepository;
		
	@BeforeEach
    void beforeEach() {

		User user;
		user = new User();
		user.setUsername(USER_USERNAME);
		user.setEmail(USER_EMAIL);
		user.setPassword(USER_PASSWORD);
		user.setFirstname(USER_FIRSTNAME);
		user.setLastname(USER_LASTNAME);
		
        try{
        	userRepository.save(user);
        }
        catch (Exception e){}
    }

	@AfterEach
    void afterEach() {

		Optional<User> userOptional = userRepository.findByUsername(USER_USERNAME);
		if (userOptional.isPresent()) {
			try {
				userRepository.delete(userOptional.get());
			}
			catch (Exception e) {}
		}
	}
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testCreateResume() throws Exception {

		mockMvc.perform(post("/profile/create")
					.with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andReturn();

		Optional<User> UserOptional = userRepository.findByUsername(USER_USERNAME);
		assertTrue(UserOptional.isPresent());
        assertNotNull(UserOptional.get().getResume());
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testAddExperience() throws Exception {

		Optional<User> UserOptional = userRepository.findByUsername(USER_USERNAME);
		assertTrue(UserOptional.isPresent());
		
		User User = UserOptional.get();
		User.setResume(new Resume());
		User.getResume().setUser(User);

		try{
        	userRepository.save(User);
        }
        catch (Exception e){}
		
		Experience experience = new Experience();
		
		mockMvc.perform(post("/profile/updateexperience")
				.flashAttr("experience", experience)
				.param("index", "999999")
				.param("profile", "update")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name(ProfileController.VIEW_PROFILE))
			.andExpect(model().attributeExists(ProfileController.AUTH_NAME))
			.andExpect(model().attributeExists(ProfileController.USER))
			.andReturn();
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testUpdateEducation() throws Exception {

		Optional<User> UserOptional = userRepository.findByUsername(USER_USERNAME);
		assertTrue(UserOptional.isPresent());
		
		User User = UserOptional.get();
		User.setResume(new Resume());
		User.getResume().setUser(User);

		try{
        	userRepository.save(User);
        }
        catch (Exception e){}
		
		Education education = new Education();
		education.setDegree("Education Degree");
		education.setUniversity("Education University");
		
		mockMvc.perform(post("/profile/saveeducation")
				.flashAttr("education", education)
				.param("profile", "save")
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andReturn();

		UserOptional = userRepository.findByUsername(USER_USERNAME);		
		Education resultEducation = UserOptional.get().getResume().getEducation().get(0);
	    assertEquals("Education Degree", resultEducation.getDegree());
	    assertEquals("Education University", resultEducation.getUniversity());
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testRemoveSkill() throws Exception {

		Skill skill = new Skill();
		skill.setName("Skill Name");
		skill.setDescription("Skill Description");
		
		ArrayList<Skill> list = new ArrayList<Skill>();
		list.add(skill);

		Optional<User> UserOptional = userRepository.findByUsername(USER_USERNAME);
		assertTrue(UserOptional.isPresent());
		
		User User = UserOptional.get();
		User.setResume(new Resume());
		User.getResume().setUser(User);
		User.getResume().setSkills(list);
		skill.setResume(User.getResume());

		try{
        	userRepository.save(User);
        }
        catch (Exception e){}
		
		mockMvc.perform(post("/profile/updateskill")
				.flashAttr("skill", skill)
				.param("index", "0")
				.param("profile", "remove")
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name(ProfileController.VIEW_PROFILE))
			.andExpect(model().attributeExists(ProfileController.AUTH_NAME))
			.andExpect(model().attributeExists(ProfileController.USER))
			.andReturn();
				
		UserOptional = userRepository.findByUsername(USER_USERNAME);		
		Skill resultSkill = UserOptional.get().getResume().getSkills().get(0);
	    assertTrue(resultSkill.getIsRemoved());
	}
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testUploadResume() throws Exception {

		Optional<User> UserOptional = userRepository.findByUsername(USER_USERNAME);
		assertTrue(UserOptional.isPresent());
		
		User User = UserOptional.get();
		User.setResume(new Resume());
		User.getResume().setUser(User);

		try{
        	userRepository.save(User);
        }
        catch (Exception e){}

		MockMultipartFile multipartFile = new MockMultipartFile("file", RESUME_FILE_NAME,
				RESUME_FILE_TYPE, RESUME_FILE_CONTENT);
		
		mockMvc.perform(multipart("/profile/upload")
				.file(multipartFile)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andReturn();
				
		UserOptional = userRepository.findByUsername(USER_USERNAME);		
		ResumeFile resultResumeFile = UserOptional.get().getResume().getResumeFile();
	    assertEquals(RESUME_FILE_NAME, resultResumeFile.getName());
	    assertEquals(RESUME_FILE_TYPE, resultResumeFile.getType());    	
	    assertTrue(Arrays.equals(RESUME_FILE_CONTENT, resultResumeFile.getContent()));    	
    }
}
