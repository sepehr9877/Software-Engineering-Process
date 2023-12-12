package ca.concordia.soen6011.controller;

import java.util.List;
import java.util.Optional;

import ca.concordia.soen6011.model.*;
import ca.concordia.soen6011.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ca.concordia.soen6011.model.Applications.ApplicationStatus;
import ca.concordia.soen6011.model.Joboffers.JobOfferStatus;


@RequestMapping("/admin")
@Controller
public class AdminController {
	
	private static final String APPLICATIONS_VIEW = "/admin/applications"; 
	private static final String JOB_OFFERS = "joboffers"; 
	private static final String UNEXPECTED_VALUE = "Unexpected value: "; 

	private final ApplicationRepository applicationRepository;
	private final JobOfferRepository jobOfferRepository;
	private final UserRepository userRepository;

	public AdminController(ApplicationRepository applicationRepository,JobOfferRepository jobOfferRepository,UserRepository userRepository) {
		this.applicationRepository=applicationRepository;
		this.jobOfferRepository=jobOfferRepository;		
		this.userRepository=userRepository;		
	}

	@PostMapping("/update_applications")
	public RedirectView adminUpdateStatusApplication(@RequestParam("applicationId") int applicationId, @RequestParam("status") String status) {
		Optional<Applications> applicationOptional = applicationRepository.findById(applicationId);
		if (applicationOptional.isEmpty()) {
			return new RedirectView(APPLICATIONS_VIEW);
		}
		Applications application = applicationOptional.get();
		if(ApplicationStatus.valueOf(status)==ApplicationStatus.ACCEPTED) {
			application.setStatus(ApplicationStatus.ACCEPTED);
		}
		else if(ApplicationStatus.valueOf(status)==ApplicationStatus.INPROGRESS) {
			application.setStatus(ApplicationStatus.INPROGRESS);
		}
		this.applicationRepository.save(application);
		return new RedirectView(APPLICATIONS_VIEW);
	}
	
	@GetMapping("/posts")
	public String adminPosts(Model model,Authentication authentication) {
		//get username and password from Authentication 
		
		List<Joboffers> joboffers=jobOfferRepository.findAll();
		model.addAttribute(JOB_OFFERS,joboffers);
		return "admin_track_posts";
	}
	
	@GetMapping("/applications")
	public String adminApplications(Model model ,@RequestParam(name = "status",required = false)String status ) {
		
		List<Applications> applications=this.applicationRepository.findAll();
		if(status!=null && !status.equals("ALL")) {
			applications=this.applicationRepository.findAllByStatus(ApplicationStatus.valueOf(status));
		}
		model.addAttribute("Applications",applications);
		return "admin_track_application";
	}
	
	@GetMapping("/deleteapplication/{id}")
	public RedirectView deleteApplication(@PathVariable("id")String id) {
		Optional<Applications> applicationsOptional=this.applicationRepository.findById(Integer.parseInt(id));
		if (applicationsOptional.isEmpty()) {
			return new RedirectView(APPLICATIONS_VIEW);
		}
		this.applicationRepository.delete(applicationsOptional.get());
		return new RedirectView(APPLICATIONS_VIEW);
	}
	
	@GetMapping("/filterapplications/{status}")
	public String adminFilterApplications(@RequestParam("status")String status,Model model) {
		List<Applications> applications=null;
		ApplicationStatus applicationStatus;
		switch (ApplicationStatus.valueOf(status)){
		case ACCEPTED:
			applicationStatus=ApplicationStatus.ACCEPTED;
			break;
		case INPROGRESS:
			applicationStatus=ApplicationStatus.INPROGRESS;
			break;
		default:
			throw new IllegalArgumentException(UNEXPECTED_VALUE + JobOfferStatus.valueOf(status));
		}
		if(applicationStatus!=null) {
			applications=this.applicationRepository.findAllByStatus(applicationStatus);
		}
		model.addAttribute("Applications",applications);
		return "admin_filter_application";
		
	}
	
	@PostMapping("/update_joboffer")
	public RedirectView adminUpdateJobofferStatus(@RequestParam(name="jobofferId") int jobofferId,@RequestParam(name="status") String status) {

		Optional<Joboffers> optionalJoboffers = this.jobOfferRepository.findById(jobofferId);
		if(optionalJoboffers.isPresent()) {
			Joboffers joboffers = optionalJoboffers.get();
			JobOfferStatus newstatus;
			switch (JobOfferStatus.valueOf(status)) {
				case ACCEPTED:
					newstatus = JobOfferStatus.ACCEPTED;
					break;
				case REJECTED:
					newstatus = JobOfferStatus.REJECTED;
					break;
				case PENDING:
					newstatus = JobOfferStatus.PENDING;
					break;
				default:
					throw new IllegalArgumentException(UNEXPECTED_VALUE + JobOfferStatus.valueOf(status));
			}
			if (newstatus != null) {
				joboffers.setStatus(newstatus);
				this.jobOfferRepository.save(joboffers);
			}
		}
	    return new RedirectView("/admin/posts");
	}
	
	@PostMapping("/filterapplication")
	public RedirectView adminFilterStatus(@RequestParam("filter") String status) {
		return new RedirectView("/admin/filterapplication/"+status);
	}
	
	@GetMapping("/filterapplication/{filter}")
	public String filterPageAdmin(@PathVariable("filter") String status,Model model) {
		JobOfferStatus newstatus;
		switch (JobOfferStatus.valueOf(status)){
		case ACCEPTED:
			newstatus=JobOfferStatus.ACCEPTED;
			break;
		case REJECTED:
			newstatus=JobOfferStatus.REJECTED;
			break;
		case PENDING:
			newstatus=JobOfferStatus.PENDING;
			break;
		default:
			throw new IllegalArgumentException(UNEXPECTED_VALUE + JobOfferStatus.valueOf(status));
		}
		List<Joboffers> joboffers=this.jobOfferRepository.findAllByStatus(newstatus);
		model.addAttribute(JOB_OFFERS,joboffers);
		return "admin_filter_joboffers";
	}
	
	@GetMapping("/searchjoboffer")
	public String searchJobs(@RequestParam("query") String item,Model model) {
		List<Joboffers> joboffers=this.jobOfferRepository.findByTitleContainingIgnoreCase(item);
		model.addAttribute(JOB_OFFERS,joboffers);
		return "search_joboffer_admin";
	}

	@GetMapping("/users")
	public String manageUsers(Model model,Authentication authentication) {
		
		List<User> users = userRepository.findAllExceptUsername(authentication.getName());
		model.addAttribute("users", users);
		return "admin_manage_users";  // This should be a view for displaying all users and providing an option to remove them
	}

	@GetMapping("/users/deactivate_user/{id}")
	public RedirectView deactivateUser(@PathVariable("id") String id) {
		User user = userRepository.findById(Integer.parseInt(id)).orElse(null);
		if(user != null) {
			user.setIsActive(false);
			userRepository.save(user);
		}
		return new RedirectView("/admin/users");
	}

	@GetMapping("/users/activate_user/{id}")
	public RedirectView activateUser(@PathVariable("id") String id) {
		User user = userRepository.findById(Integer.parseInt(id)).orElse(null);
		if(user != null) {
			user.setIsActive(true);
			userRepository.save(user);
		}
		return new RedirectView("/admin/users");
	}
}
