package ca.concordia.soen6011.controller;

import java.io.IOException;
import java.util.Optional;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Education;
import ca.concordia.soen6011.model.Experience;
import ca.concordia.soen6011.model.Skill;
import ca.concordia.soen6011.model.Resume;
import ca.concordia.soen6011.model.ResumeFile;
import ca.concordia.soen6011.repository.UserRepository;
import ca.concordia.soen6011.repository.EducationRepository;
import ca.concordia.soen6011.repository.ExperienceRepository;
import ca.concordia.soen6011.repository.ResumeFileRepository;
import ca.concordia.soen6011.repository.SkillRepository;


/**
 * This controller handles page requests for user profiles
 */
@RequestMapping("/profile")
@Controller
public class ProfileController {

	/** Tag to store authenticated name */
	public static final String AUTH_NAME = "authname";
	/** Tag to store user */
	public static final String USER = "user";

	/** Company view */
	public static final String VIEW_COMPANY = "company"; 
	/** Profile view */
	public static final String VIEW_PROFILE = "profile"; 
	/** Redirect to home */
	public static final String VIEW_REDIRECT = "redirect:/"; 
	/** Redirect to profile */
	public static final String VIEW_REDIRECT_PROFILE = "redirect:/profile/"; 
	/** Error view */
	public static final String VIEW_ERROR = "error"; 

	/** Form Action Remove */
	public static final String FORM_ACTION_REMOVE = "remove";
	/** Form Action Save */
	public static final String FORM_ACTION_SAVE = "save";
	/** Form Action Update */
	public static final String FORM_ACTION_UPDATE = "update";
	
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final ResumeFileRepository resumeFileRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    
	/**
	 * Create a Profilecontroller with the repositories
	 * @param userRepository repository to store User
	 * @param educationRepository repository to store education
	 * @param experienceRepository repository to store experience
	 * @param resumeFileRepository repository to store resume files
	 * @param skillRepository repository to store skill
	 * @param userRepository repository to store user
	 */
	public ProfileController(
			EducationRepository educationRepository, 
			ExperienceRepository experienceRepository,
			ResumeFileRepository resumeFileRepository,
			SkillRepository skillRepository,
			UserRepository userRepository) {

		this.educationRepository = educationRepository;
		this.experienceRepository = experienceRepository;
		this.resumeFileRepository = resumeFileRepository;
		this.skillRepository = skillRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Returns the default index page
	 * @param userDetails the authenticated user
	 * @return the index page
	 */
	@GetMapping
	public String getIndexView(@AuthenticationPrincipal UserDetails userDetails, Model model) {

		return VIEW_REDIRECT;
	}

	/**
	 * Displays a user profile with all information
	 * @param username the username to look up
	 * @param userDetails the authenticated user
	 * @param model the username to look up
	 * @return the profile page
	 */
    @GetMapping("/{username}")
    public String getProfileView(
    		@PathVariable("username")String username,
    		@AuthenticationPrincipal UserDetails userDetails, 
    		Model model) {
    	
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
        	
        	User c = userOptional.get();
			updateModel(userDetails, model, c);
        	return VIEW_PROFILE;
        }

        return VIEW_REDIRECT;
    }
    
  	/**
  	 * Uploads a file for the resume
	 * @param userDetails the authenticated user
  	 * @param model the model to store data
  	 * @return the profile page
  	 */
  	@PostMapping({"/upload"})
  	public String uploadFile(
  			@RequestParam("file") MultipartFile multipartFile,
  			@AuthenticationPrincipal UserDetails userDetails,
  			Model model) throws IOException {

  		if (multipartFile.isEmpty() || multipartFile.getOriginalFilename() == null) {

  			return VIEW_ERROR;
  		}

  		String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }
  				
	  	User c = userOptional.get();
	  	if (c.getResume() != null) {
	
	  		ResumeFile resumeFile = new ResumeFile();
	  		
	  		resumeFile.setName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
	  		resumeFile.setType(multipartFile.getContentType());
	  		resumeFile.setContent(multipartFile.getBytes());
	  		resumeFile.setResume(c.getResume());
	  		c.getResume().setResumeFile(resumeFile);
	  				  		
			userRepository.save(c);
	  	}
	
		updateModel(userDetails, model, c);   
		  
		return VIEW_REDIRECT_PROFILE + username;
	}
    	
	/**
	 * Returns the default index page
	 * @param fileId the file to download
	 * @param userDetails the authenticated user
	 * @return the index page
	 */
  	@GetMapping(value = "/file/{fileId}", produces = MediaType.APPLICATION_PDF_VALUE)
  	ResponseEntity<Resource> downloadFile(@PathVariable int fileId,
  			@AuthenticationPrincipal UserDetails userDetails) {
  		
  		String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
        	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
  		
  	    ResumeFile resumeFile = resumeFileRepository.findById(fileId)
  	      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
  	    
  	    Resource file = new ByteArrayResource(resumeFile.getContent(), resumeFile.getName());
  	    
  	    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + resumeFile.getName() + "\"").body(file);
  	}
  	
	/**
	 * Creates a resume and redirects to the profile page
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/create"})
	public String createResume(
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

    	User c = userOptional.get();
    	if (c.getResume() == null) {
    		 
    	   	c.setResume(new Resume());
    		c.getResume().setUser(c);
    		userRepository.save(c); 
    	}

		updateModel(userDetails, model, c);   
        
        return VIEW_REDIRECT_PROFILE + username;
	}

	/**
	 * Show an update form for the education
	 * @param index the index of the object in the database
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/updateeducation"})
	public String updateEducation(
			@RequestParam("index") Integer index,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {
		
    	String username = userDetails.getUsername();
		
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

        User c = userOptional.get();
		Resume r = c.getResume();	        					

		int educationIndex = index;
		Education education = null;
		if (educationIndex < r.getEducation().size()) {
            education = r.getEducation().get(educationIndex);
		}
		
		if (profile.equals(FORM_ACTION_REMOVE)) {

			if (education == null) {
	            return VIEW_ERROR;
			}

			education.setIsRemoved(true);
			educationRepository.save(education);
			updateModel(userDetails, model, c);				
		}
		else { // update

			if (education == null) {
	            education = new Education();
				education.setResume(c.getResume());
			}

    		model.addAttribute("education", education);
			updateModel(userDetails, model, c, UpdateField.EDUCATION);
		}
 
        return VIEW_PROFILE;
	}

	/**
	 * Save the changes to the Education
	 * @param education the Education to saved
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/saveeducation"})
	public String saveEducation(@ModelAttribute("education") Education education,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();

		Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

		User c = userOptional.get();
		education.setResume(c.getResume());

		if (profile.contains(FORM_ACTION_SAVE)) {

			int educationId = education.getId();
			if (educationId != 0) {
				if (c.getResume().getEducation() == null) {
			        return VIEW_ERROR;
				}
	
				Education dbEducation = c.getResume().getEducation().stream()
						  .filter(e -> educationId == e.getId())
						  .findAny()
						  .orElse(null);
				if (dbEducation == null) {
			        return VIEW_ERROR;
				}
			}

			educationRepository.save(education);
		}

		updateModel(userDetails, model, c);
          
        return VIEW_REDIRECT_PROFILE + username;
	}

	/**
	 * Show an update form for the Experience
	 * @param index the index of the object in the database
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/updateexperience"})
	public String updateExperience(
			@RequestParam("index") Integer index,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();
		
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

    	User c = userOptional.get();
		Resume r = c.getResume();

		int experienceIndex = index;
		Experience experience = null;
		if (experienceIndex < r.getExperience().size()) {
            experience = r.getExperience().get(experienceIndex);
		}
		
		if (profile.contains(FORM_ACTION_REMOVE)) {

			if (experience == null) {
	            return VIEW_ERROR;
			}

			experience.setIsRemoved(true);
			experienceRepository.save(experience);
			updateModel(userDetails, model, c);				
		}
		else { // update

			if (experience == null) {
				experience = new Experience();
				experience.setResume(c.getResume());
			}
			
    		model.addAttribute("experience", experience);
			updateModel(userDetails, model, c, UpdateField.EXPERIENCE);
		}

        return VIEW_PROFILE;
	}

	/**
	 * Save the changes to the Experience
	 * @param experience the Experience to save
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/saveexperience"})
	public String save(
			@ModelAttribute("experience") Experience experience,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();

		Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

		User c = userOptional.get();
		experience.setResume(c.getResume());

		if (profile.contains(FORM_ACTION_SAVE)) {

			int experienceId = experience.getId();
			if (experienceId != 0) {
				
				if (c.getResume().getExperience() == null) {
			        return VIEW_ERROR;
				}
	
				Experience dbExperience = c.getResume().getExperience().stream()
						  .filter(e -> experienceId == e.getId())
						  .findAny()
						  .orElse(null);
				if (dbExperience == null) {
			        return VIEW_ERROR;
				}
			}

			experienceRepository.save(experience);
		}
 
		updateModel(userDetails, model, c);
        
        return VIEW_REDIRECT_PROFILE + username;
	}

	/**
	 * Show an update form for the Skill
	 * @param index the index of the Skill object in the database
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/updateskill"})
	public String updateSkill(
			@RequestParam("index") Integer index,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();
		 
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

    	User c = userOptional.get();
    	Resume r = c.getResume();

		int skillIndex = index;
		Skill skill = null;
		if (skillIndex < r.getSkills().size()) {
			skill = r.getSkills().get(skillIndex);
		}
		
		if (profile.contains(FORM_ACTION_REMOVE)) {
			
			if (skill == null) {
	            return VIEW_ERROR;
			}
			
			skill.setIsRemoved(true);
			skillRepository.save(skill);
			updateModel(userDetails, model, c);				
		}
		else { // update
			
			if (skill == null) {
				skill = new Skill();
				skill.setResume(c.getResume());
			}
			
    		model.addAttribute("skill", skill);
			updateModel(userDetails, model, c, UpdateField.SKILL);
		}
 
        return VIEW_PROFILE;
	}

	/**
	 * Save the changes to the Skill
	 * @param skill the Skill to save
	 * @param profile profile the type of action
	 * @param userDetails the authenticated user
	 * @param model the model to store data
	 * @return the profile page
	 */
	@PostMapping({"/saveskill"})
	public String save(
			@ModelAttribute("skill") Skill skill,
			@RequestParam("profile") String profile,
			@AuthenticationPrincipal UserDetails userDetails,
			Model model) {

    	String username = userDetails.getUsername();

		Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            return VIEW_ERROR;
        }

		User c = userOptional.get();
		skill.setResume(c.getResume());
		
		if (profile.contains(FORM_ACTION_SAVE)) {
			
			int skillId = skill.getId();
			if (skillId != 0) {

				if (c.getResume().getSkills() == null) {
			        return VIEW_ERROR;
				}
	
				Skill dbSkill = c.getResume().getSkills().stream()
						  .filter(s -> skillId == s.getId())
						  .findAny()
						  .orElse(null);
				if (dbSkill == null) {
			        return VIEW_ERROR;
				}
			}
			
			skillRepository.save(skill);
		}
		
		updateModel(userDetails, model, c);
         
        return VIEW_REDIRECT_PROFILE + username;
	}

	/** 
	 * The Field to Update
	 */
	public enum UpdateField {
		
		/** No field */
		NONE,
		/** Education field */
		EDUCATION,
		/** Experience field */
		EXPERIENCE,
		/** Skill field */
		SKILL;
	}
 
	/**
	 * Clear the update field for the form to edit
	 * @param model the model to store data
	 * @param User the User to store in the model
	 */
	private void updateModel(UserDetails userDetails, Model model,
			User user) {
		
		model.addAttribute(AUTH_NAME, userDetails.getUsername());
		model.addAttribute(USER, user);
		model.addAttribute("updateeducation", false);
		model.addAttribute("updateexperience", false);
		model.addAttribute("updateskill", false);
		model.addAttribute("updatefield", UpdateField.NONE);
	}

	/**
	 * Set the update field for the form to edit
	 * @param model the model to store data
	 * @param User the User to store in the model
	 * @param updateField the type of field that will be edited
	 */
	private void updateModel(UserDetails userDetails, Model model,
			User user, UpdateField updateField) {

		model.addAttribute(AUTH_NAME, userDetails.getUsername());
		model.addAttribute(USER, user);
		model.addAttribute("updateeducation", updateField == UpdateField.EDUCATION);
		model.addAttribute("updateexperience", updateField == UpdateField.EXPERIENCE);
		model.addAttribute("updateskill", updateField == UpdateField.SKILL);
		model.addAttribute("updatefield", updateField);
	}
}