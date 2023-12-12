package ca.concordia.soen6011.controller;

import jakarta.validation.constraints.NotNull;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ca.concordia.soen6011.repository.CompanyRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.UserRepository;
import ca.concordia.soen6011.model.Company;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Joboffers.JobOfferStatus;

import org.springframework.ui.Model;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/company")
public class AddJobOfferController {

	private static final String REDIRECT_JOBOFFERS = "redirect:/company/myJobOffers";
	private static final String REDIRECT_LOGIN = "redirect:/login";

	private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public AddJobOfferController(JobOfferRepository jobOfferRepository, UserRepository userRepository, CompanyRepository companyRepository) {
        this.jobOfferRepository=jobOfferRepository;
        this.userRepository=userRepository;
        this.companyRepository=companyRepository;
    }
    
    @GetMapping("/addJobOffer")
    public String addJobOffer()
    {
        return "add-job-page";
    }

    @PostMapping("/saveJobOffer")
    public String saveJobOffer(@RequestParam("title") String title
            , @RequestParam("description") String description
            , @RequestParam("staff") int staff
            , @RequestParam("company") String company
            , @RequestParam("published") LocalDate published
            , @RequestParam("deadline") LocalDate deadline
            , @RequestParam("lowsalery") int lowsalery
            , @RequestParam("highsalery") int highsalery
            , @AuthenticationPrincipal UserDetails userDetails) {

        String username = userDetails.getUsername();

        Optional<User> userOptional = userRepository.findByUsername(username);
        Joboffers joboffers = new Joboffers(title, description, staff, company, published,null,
                highsalery,lowsalery, deadline, JobOfferStatus.PENDING);
        Company selectedCompany=null;
        if (userOptional.isPresent()) {
        	
        	if(userOptional.get().getCompany()!=null)
        		selectedCompany = userOptional.get().getCompany();
        	else {
        		selectedCompany=new Company(userOptional.get(),company);
        		this.companyRepository.save(selectedCompany);
        		
        	}
            
        	joboffers.setCompany(selectedCompany);
            jobOfferRepository.save(joboffers);
            return REDIRECT_JOBOFFERS;
        }
        else
            return REDIRECT_LOGIN;
    }

    @GetMapping("/myJobOffers")
    public String myJobOffers(@NotNull Model model,Authentication authentication)
    {
    	Optional<User> userOptional=this.userRepository.findByUsername(authentication.getName());
    	if (userOptional.isEmpty()) {
            return "error";
    	}
    	List<Joboffers> myJobOffers=null;
    	User user = userOptional.get();
    	if(user.getCompany()!=null) {
    		myJobOffers = jobOfferRepository.findByCompanyId(user.getCompany().getId());}

        
        model.addAttribute("myJobOffers", myJobOffers);

        return "my-job-offers";
    }
    

    @GetMapping("/deleteJobOffer/{id}")
    public String deleteJobOffer(@PathVariable("id") int id, @AuthenticationPrincipal UserDetails userDetails) {
       
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            Optional<Joboffers> jobOfferOptional = jobOfferRepository.findById(id);
            if (jobOfferOptional.isPresent()) {
                Joboffers jobOffer = jobOfferOptional.get();
                jobOffer.setStatus(JobOfferStatus.REJECTED); // Change the status to the desired value (e.g., CANCELLED).
                jobOfferRepository.save(jobOffer); // Save the updated job offer to the database.
            }
            userOptional.get().getCompany().getId();
            return REDIRECT_JOBOFFERS; // Redirect back to the list of job offers.
        } else {
            return REDIRECT_LOGIN;
        }
    }


    @GetMapping("/editJobOffer/{id}")
    public String editJobOffer(@PathVariable("id") int id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
       
        Optional<Joboffers> jobOfferOptional = jobOfferRepository.findById(id);
        if (jobOfferOptional.isPresent()) {
            Joboffers jobOffer = jobOfferOptional.get();
            model.addAttribute("jobOffer", jobOffer);
            return "edit-job-page"; // Return the add-job-page template with the pre-filled data.
        } else {
            // Handle case when the job offer with the given ID does not exist
            String username = userDetails.getUsername();
            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isPresent()) {
                userOptional.get().getCompany().getId();
                return REDIRECT_JOBOFFERS; // Redirect to the list of job offers.
            }
            else
                return REDIRECT_LOGIN;
        }
    }

    @PostMapping("/editJobOffer")
    public String editJobOffer(@RequestParam("jobOfferId") Integer jobOfferId,
                               @RequestParam("title") String title,
                               @RequestParam("description") String description,
                               @RequestParam("staff") int staff,
                               @RequestParam("company") String company,
                               @RequestParam("published") LocalDate published,
                               @RequestParam("deadline") LocalDate deadline,
                               @RequestParam("lowsalery") int lowsalery,
                               @RequestParam("highsalery") int highsalery,
                               @AuthenticationPrincipal UserDetails userDetails) {

        // Check if jobOfferId is provided (not null) to ensure it's an edit operation.
      
        if (jobOfferId != null) {
            // If jobOfferId is provided, update the existing job offer.
            Optional<Joboffers> jobOfferOptional = jobOfferRepository.findById(jobOfferId);
            if (jobOfferOptional.isPresent()) {
                Joboffers jobOffer = jobOfferOptional.get();

            	Company selectedCompany=jobOffer.getCompany();
            	selectedCompany.setName(company);
                jobOffer.setTitle(title);
                jobOffer.setDescription(description);
                jobOffer.setStaff(staff);
                jobOffer.setCompanyName(company);
                jobOffer.setPublished(published);
                jobOffer.setDeadline(deadline);
                jobOffer.setLowsalery(lowsalery);
                jobOffer.setHighsalery(highsalery);
                // Save the updated job offer to the database.
                jobOfferRepository.save(jobOffer);
                this.companyRepository.save(selectedCompany);

//                return "redirect:/company/editJobOffer/"+jobOfferId; // Redirect back to the list of job offers after the update.
            }
        }

        // Handle case when the job offer with the given ID does not exist or jobOfferId is not provided.
        String username = userDetails.getUsername();
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            userOptional.get().getCompany().getId();
            return REDIRECT_JOBOFFERS; // Redirect to the list of job offers.
        }
        else
            return REDIRECT_LOGIN;
    }
}
