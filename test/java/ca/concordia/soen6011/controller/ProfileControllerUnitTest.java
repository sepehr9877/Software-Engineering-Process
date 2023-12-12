package ca.concordia.soen6011.controller;

import java.util.Optional;
import java.util.ArrayList;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Education;
import ca.concordia.soen6011.model.Experience;
import ca.concordia.soen6011.model.Skill;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.ResumeFile;
import ca.concordia.soen6011.repository.CompanyRepository;
import ca.concordia.soen6011.repository.EducationRepository;
import ca.concordia.soen6011.repository.ExperienceRepository;
import ca.concordia.soen6011.repository.ResumeFileRepository;
import ca.concordia.soen6011.repository.SkillRepository;
import ca.concordia.soen6011.repository.UserRepository;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithAnonymousUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class ProfileControllerUnitTest {

	private static final int USER_ID = 4;
	private static final String USER_USERNAME = "TheTestMaster";

	private static final int RESUME_FILE_ID = 1;
	private static final String RESUME_FILE_NAME = "Resume File Name";
	private static final String RESUME_FILE_TYPE = "Resume File Type";
	private static final byte[] RESUME_FILE_CONTENT = "Resume File Content".getBytes();

	@Mock
	private EducationRepository educationRepository; 
	@Mock
	private CompanyRepository companyRepository; 
	@Mock
	private ExperienceRepository experienceRepository; 
	@Mock
	private ResumeFileRepository resumeFileRepository; 
	@Mock
	private SkillRepository skillRepository; 
	@Mock
	private UserRepository userRepository; 

	@Mock
	private UserDetails userDetails;

	@Mock
	private Model model;

	@InjectMocks
	private ProfileController controller;

	@BeforeEach
	void beforeEach() {
	}
	
    @Test
    void testIndexView() {

    	String result = controller.getIndexView(userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT, result);
    }

    @Test
    void testProfileViewWithNoUser() {
    	    	
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.empty());
    	    	
        String result = controller.getProfileView(USER_USERNAME, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT, result);
    }

    @Test
    void testProfileViewWithUser() {
    	    	
    	User c = setupUser();

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.getProfileView(USER_USERNAME, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        verify(model, times(1)).addAttribute(anyString(), anyString());
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testCreateResume() {
    	    	
    	User c = setupUser();
    	c.setResume(null);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.createResume(userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        assertNotNull(c.getResume());
        verify(model, times(1)).addAttribute(anyString(), anyString());
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateResumeNotLoggedIn() {
    	    	
    	User c = new User();
    	c.setId(USER_ID);
    	c.setUsername(USER_USERNAME);

    	when(userDetails.getUsername()).thenReturn("");

        String result = controller.createResume(userDetails, model);
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testUpdateEducationNotLoggedIn() {
    	    	
    	when(userDetails.getUsername()).thenReturn("");

    	String result = controller.updateEducation(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testUpdateEducationWithUpdate() {
    	    	
    	User c = setupUser();
    	Education e = new Education();
    	c.getResume().setEducation(new ArrayList<Education>());
    	c.getResume().getEducation().add(e);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateEducation(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUpdateEducationWithDelete() {
    	    	
    	User c = setupUser();
    	Education e = new Education();
    	c.getResume().setEducation(new ArrayList<Education>());
    	c.getResume().getEducation().add(e);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateEducation(0, ProfileController.FORM_ACTION_REMOVE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);
        
        assertTrue(e.getIsRemoved());

        verify(educationRepository, times(1)).save(any(Education.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testSaveEducationNotLoggedIn() {
    	    	
    	User c = new User();
    	c.setId(USER_ID);
    	c.setUsername(USER_USERNAME);
    	c.setResume(new Resume());
    	
    	Education e = new Education();

    	when(userDetails.getUsername()).thenReturn("");
    	
        String result = controller.saveEducation(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testSaveEducationNewWithSave() {
    	    	 
    	User c = setupUser();
    	Education e = new Education();
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.saveEducation(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(educationRepository, times(1)).save(any(Education.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testSaveEducationExistingWithSave() {
    	    	 
    	User c = setupUser();
    	Education e = new Education();
    	e.setId(1);
    	e.setResume(c.getResume());
    	
    	ArrayList<Education> list = new ArrayList<Education>();
    	list.add(e);
    	c.getResume().setEducation(list);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.saveEducation(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(educationRepository, times(1)).save(any(Education.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUpdateExperienceNotLoggedIn() {
    	    	
    	when(userDetails.getUsername()).thenReturn("");
    	
        String result = controller.updateExperience(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testUpdateExperienceWithUpdate() {
    	    	
    	User c = setupUser();
    	Experience e = new Experience();
    	c.getResume().setExperience(new ArrayList<Experience>());
    	c.getResume().getExperience().add(e);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateExperience(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUpdateExperienceWithRemove() {
    	    	
    	User c = setupUser();
    	Experience e = new Experience();
    	c.getResume().setExperience(new ArrayList<Experience>());
    	c.getResume().getExperience().add(e);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateExperience(0, ProfileController.FORM_ACTION_REMOVE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        assertTrue(e.getIsRemoved());

        verify(experienceRepository, times(1)).save(any(Experience.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    @WithAnonymousUser
    void testSaveExperienceNotLoggedIn() {
    	    	
    	Experience e = new Experience();

    	when(userDetails.getUsername()).thenReturn("");
    	
        String result = controller.save(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testSaveExperienceNewWithSave() {
    	    	 
    	User c = setupUser();
    	Experience e = new Experience();
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.save(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(experienceRepository, times(1)).save(any(Experience.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testSaveExperienceExistingWithSave() {
    	    	 
    	User c = setupUser();
    	Experience e = new Experience();
    	e.setId(1);
    	e.setResume(c.getResume());
    	
    	ArrayList<Experience> list = new ArrayList<Experience>();
    	list.add(e);
    	c.getResume().setExperience(list);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.save(e, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(experienceRepository, times(1)).save(any(Experience.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUpdateSkillNotLoggedIn() {
    	    	
    	when(userDetails.getUsername()).thenReturn("");
    	
        String result = controller.updateSkill(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testUpdateSkillWithUpdate() {
    	    	
    	User c = setupUser();
    	Skill s = new Skill();
    	c.getResume().setSkills(new ArrayList<Skill>());
    	c.getResume().getSkills().add(s);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateSkill(0, ProfileController.FORM_ACTION_UPDATE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUpdateSkillWithRemove() {
    	    	
    	User c = setupUser();
    	Skill s = new Skill();
    	s.setResume(c.getResume());
    	c.getResume().setSkills(new ArrayList<Skill>());
    	c.getResume().getSkills().add(s);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.updateSkill(0, ProfileController.FORM_ACTION_REMOVE, userDetails, model);
        assertEquals(ProfileController.VIEW_PROFILE, result);

        assertTrue(s.getIsRemoved());

        verify(skillRepository, times(1)).save(any(Skill.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testSaveSkillNotLoggedIn() {
    	    	
    	Skill s = new Skill();

    	when(userDetails.getUsername()).thenReturn("");

        String result = controller.save(s, ProfileController.FORM_ACTION_REMOVE, userDetails, model);
    	
        assertEquals(ProfileController.VIEW_ERROR, result);
    }

    @Test
    void testSaveSkillNewWithSave() {
    	    	 
    	User c = setupUser();
    	Skill s = new Skill();
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.save(s, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(skillRepository, times(1)).save(any(Skill.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testSaveSkillExistingWithSave() {
    	    	 
    	User c = setupUser();
    	Skill s = new Skill();
    	s.setId(1);
    	s.setResume(c.getResume());
    	
    	ArrayList<Skill> list = new ArrayList<Skill>();
    	list.add(s);
    	c.getResume().setSkills(list);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(c));
    	
        String result = controller.save(s, ProfileController.FORM_ACTION_SAVE, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(skillRepository, times(1)).save(any(Skill.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUploadFile() throws Exception {

    	User User = setupUser();
		MockMultipartFile multipartFile = new MockMultipartFile("file", RESUME_FILE_NAME,
				RESUME_FILE_TYPE, RESUME_FILE_CONTENT);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(User));
    	
        String result = controller.uploadFile(multipartFile, userDetails, model);
        assertEquals(ProfileController.VIEW_REDIRECT_PROFILE + USER_USERNAME, result);

        verify(userRepository, times(1)).save(any(User.class));
        verify(model, times(1)).addAttribute(anyString(), any(User.class));
    }

    @Test
    void testUploadFileWithEmptyFile() throws Exception {

		MockMultipartFile multipartFile = new MockMultipartFile( "file", RESUME_FILE_NAME,
				RESUME_FILE_TYPE, RESUME_FILE_CONTENT);
    	
        String result = controller.uploadFile(multipartFile, userDetails, model);
        assertEquals("error", result);
    }
	
    @Test
    void testDownloadFile() throws Exception {

    	User User = setupUser();
    	
		ResumeFile resumeFile = new ResumeFile();
		resumeFile.setId(RESUME_FILE_ID);
		resumeFile.setName(RESUME_FILE_NAME);
		resumeFile.setType(RESUME_FILE_TYPE);
		resumeFile.setContent(RESUME_FILE_CONTENT);
    	
    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(User));
    	when(resumeFileRepository.findById(RESUME_FILE_ID)).thenReturn(Optional.of(resumeFile));
    	
        ResponseEntity<Resource> result = controller.downloadFile(RESUME_FILE_ID, userDetails);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Byte array resource [" + RESUME_FILE_NAME + "]", result.getBody().getDescription());
    }
	
    @Test
    void testDownloadFileWithNoUser() throws Exception {

    	when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.empty());
    	
    	assertThrows(ResponseStatusException.class, () -> {
    		controller.downloadFile(RESUME_FILE_ID, userDetails);
    	});
    }
	
    @Test
    void testDownloadFileInvalid() throws Exception {
 
    	User User = setupUser();
    	
		ResumeFile resumeFile = new ResumeFile();
		resumeFile.setId(RESUME_FILE_ID);
		resumeFile.setName(RESUME_FILE_NAME);
		resumeFile.setType(RESUME_FILE_TYPE);
		resumeFile.setContent(RESUME_FILE_CONTENT);

		when(userDetails.getUsername()).thenReturn(USER_USERNAME);
    	when(userRepository.findByUsername(USER_USERNAME)).thenReturn(Optional.of(User));    	
    	when(resumeFileRepository.findById(RESUME_FILE_ID)).thenReturn(Optional.empty());
    	
    	assertThrows(ResponseStatusException.class, () -> {
    		controller.downloadFile(RESUME_FILE_ID, userDetails);
    	 });
	}
     
	private User setupUser() {

		User c = new User();
		c.setId(USER_ID);
		c.setUsername(USER_USERNAME);
		c.setResume(new Resume());
		return c;
	}
}