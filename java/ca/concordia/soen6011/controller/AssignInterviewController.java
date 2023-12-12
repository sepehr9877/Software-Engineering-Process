package ca.concordia.soen6011.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Interview;
import ca.concordia.soen6011.model.Interview.InterviewType;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.UserRepository;
import ca.concordia.soen6011.repository.InterviewRepository;

/**
 * Controller class for assigning and managing interviews for employers.
 */
@Controller
@RequestMapping("/company/")
public class AssignInterviewController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterviewRepository interviewRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    /**
     * GET request handler for browsing candidates and applications.
     * 
     * @param model the model to be populated with data
     * @return the view name for displaying the browse candidates page
     */
    @GetMapping("browsecandidate")
    public String browse_candidate(Model model,Authentication authentication) {
    	String username=authentication.getName();
    	
        List<User> users = this.userRepository.findAll().stream().filter(user->!user.getUsername().equals(username)).collect(Collectors.toList());
        model.addAttribute("candidates", users);
        List<Applications> applications = this.applicationRepository.findAll().stream().filter(application->!application.getUser().getUsername().equals(username)).collect(Collectors.toList());
        model.addAttribute("applications", applications);
        return "employer_browse_candidates";
    }

    /**
     * POST request handler for deleting an interview.
     * 
     * @param application_id the ID of the application
     * @return a RedirectView to the browse candidates page
     */
    @PostMapping("deleteinterview/{id}")
    public RedirectView delete_interview(@PathVariable("id") String application_id) {
        Applications application = this.applicationRepository.findById(Integer.parseInt(application_id)).get();
        Interview interview = application.getInterview();
        application.setInterview(null);
        this.interviewRepository.delete(interview);
        this.applicationRepository.save(application);
        return new RedirectView("/company/browsecandidate");
    }

    /**
     * POST request handler for assigning or updating an interview.
     * 
     * @param datetime      the date and time of the interview
     * @param applicationId the ID of the application
     * @param type          the type of the interview
     * @return a RedirectView to the browse candidates page
     */
    @PostMapping("submitinterview")
    public RedirectView assign_interview(@RequestParam("datetime") LocalDateTime datetime,
            @RequestParam("applicaton_id") String applicationId, @RequestParam("type") String type) {
        Applications application = this.applicationRepository.findById(Integer.parseInt(applicationId)).get();
        LocalDate date = datetime.toLocalDate();
        LocalTime time = datetime.toLocalTime();
        if (application.getInterview() == null) {
            Interview interview = new Interview(date, time, InterviewType.valueOf(type));
            this.interviewRepository.save(interview);
            application.setInterview(interview);
        } else {
            Interview selected_interview = application.getInterview();
            selected_interview.setDate(date);
            selected_interview.setTime(time);
            this.interviewRepository.save(selected_interview);
            application.setInterview(selected_interview);
        }
        this.applicationRepository.save(application);
        return new RedirectView("/company/browsecandidate");
    }

}
