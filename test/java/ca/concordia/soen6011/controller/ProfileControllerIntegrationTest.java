package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.configuration.WebSecurityConfig;
import ca.concordia.soen6011.model.Education;
import ca.concordia.soen6011.model.Experience;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.Skill;
import ca.concordia.soen6011.repository.UserRepository;
import ca.concordia.soen6011.repository.EducationRepository;
import ca.concordia.soen6011.repository.CompanyRepository;
import ca.concordia.soen6011.repository.ExperienceRepository;
import ca.concordia.soen6011.repository.ResumeFileRepository;
import ca.concordia.soen6011.repository.SkillRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@Import(WebSecurityConfig.class)
@WebMvcTest(ProfileController.class)
@ExtendWith(MockitoExtension.class)
class ProfileControllerIntegrationTest {

	private static final int USER_ID = 4;
	private static final String USER_USERNAME = "TheTestMaster";

	private static final String RESUME_FILE_NAME = "Resume File Name";
	private static final String RESUME_FILE_TYPE = "Resume File Type";
	private static final byte[] RESUME_FILE_CONTENT = "Resume File Content".getBytes();

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EducationRepository educationRepository;
	
	@MockBean
	private CompanyRepository companyRepository;
	
	@MockBean
	private ExperienceRepository experienceRepository;
	
	@MockBean
	private ResumeFileRepository resumeFileRepository;
	
	@MockBean
	private SkillRepository skillRepository; 
	
	@MockBean
	private UserRepository userRepository; 

	private User user;
	
	@BeforeEach
	void init() {
		
		user = new User();
		user.setId(USER_ID);
		user.setUsername(USER_USERNAME);
		user.setResume(new Resume());
		user.getResume().setUser(user);
	}

	@Test
	@WithMockUser(USER_USERNAME)
	void testIndexView() throws Exception {

		// 1. Verifying HTTP Request Matching
		mockMvc.perform(get("/profile"))
				.andExpect(status().is3xxRedirection())
				.andReturn();
	}

    @Test
	@WithMockUser(USER_USERNAME)
    void testProfileViewWithNoUser() throws Exception {
    	    	
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.empty());
    	
		// 1. Verifying HTTP Request Matching
		mockMvc.perform(get("/profile/" + USER_USERNAME))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(ProfileController.VIEW_REDIRECT))
				.andReturn();
    }

    @Test
	@WithMockUser(USER_USERNAME)
    void testProfileViewWithUser() throws Exception {
    	    	
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
    	
		// 1. Verifying HTTP Request Matching
		mockMvc.perform(get("/profile/" + USER_USERNAME))
				.andExpect(status().isOk())
				.andExpect(view().name(ProfileController.VIEW_PROFILE))
				.andExpect(model().attributeExists(ProfileController.AUTH_NAME))
				.andExpect(model().attributeExists(ProfileController.USER))
				.andReturn();
    }

    @Test
	@WithMockUser(USER_USERNAME)
    void testCreateResume() throws Exception {
    	    	
    	user.setResume(null);
    	
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
    	
		// 1. Verifying HTTP Request Matching
		mockMvc.perform(post("/profile/create").with(csrf()))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME))
				.andReturn();

        verify(userRepository, times(1)).save(any(User.class));
        assertNotNull(user.getResume());
    }
    	
    @Test
	@WithMockUser(USER_USERNAME)
    void testSaveEducationWithSave() throws Exception {
		
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
		
		Education education = new Education();
		education.setDegree("Education Degree");
		education.setUniversity("Education University");
		
		mockMvc.perform(post("/profile/saveeducation")
				.flashAttr("education", education)
				.param("profile", ProfileController.FORM_ACTION_SAVE)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andReturn();

		verify(educationRepository, times(1)).save(any(Education.class));
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testUpdateExperienceWithUpdate() throws Exception {
				
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
		
		ArrayList<Experience> list = new ArrayList<Experience>();
		
		user.getResume().setExperience(list);
		
		mockMvc.perform(post("/profile/updateexperience")
				.param("index", "0")
				.param("profile", ProfileController.FORM_ACTION_UPDATE)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name(ProfileController.VIEW_PROFILE))
			.andExpect(model().attributeExists(ProfileController.AUTH_NAME))
			.andExpect(model().attributeExists(ProfileController.USER))
			.andReturn();
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testUpdateRemoveSkillWithRemove() throws Exception {

		Skill skill = new Skill();
		skill.setName("Skill Name");
		skill.setDescription("Skill Description");
		
		ArrayList<Skill> list = new ArrayList<Skill>();
		list.add(skill);
		
		user.getResume().setSkills(list);
		skill.setResume(user.getResume());

    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));
		
		mockMvc.perform(post("/profile/updateskill")
				.param("index", "0")
				.param("profile", ProfileController.FORM_ACTION_REMOVE)
				.with(csrf()))
			.andExpect(status().isOk())
			.andExpect(view().name(ProfileController.VIEW_PROFILE))
			.andExpect(model().attributeExists(ProfileController.AUTH_NAME))
			.andExpect(model().attributeExists(ProfileController.USER))
			.andReturn();
				
		verify(skillRepository, times(1)).save(any(Skill.class));

        assertTrue(skill.getIsRemoved());
    }
	
    @Test
	@WithMockUser(USER_USERNAME)
    void testUploadFile() throws Exception {

    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(user));

		MockMultipartFile multipartFile = new MockMultipartFile("file", RESUME_FILE_NAME,
				RESUME_FILE_TYPE, RESUME_FILE_CONTENT);
		
		mockMvc.perform(multipart("/profile/upload")
				.file(multipartFile)
				.with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andReturn();
				
        verify(userRepository, times(1)).save(any(User.class));
    }

}