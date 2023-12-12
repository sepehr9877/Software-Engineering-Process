package ca.concordia.soen6011.controller;


import ca.concordia.soen6011.model.*;
import ca.concordia.soen6011.repository.ApplicationRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ca.concordia.soen6011.repository.JobOfferRepository;
import ca.concordia.soen6011.repository.UserRepository;



import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/applicant")
public class BrowseJobOffersController {

    private final JobOfferRepository jobOfferRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;

    public BrowseJobOffersController(JobOfferRepository jobOfferRepository,UserRepository userRepository,ApplicationRepository applicationRepository) {
        this.jobOfferRepository=jobOfferRepository;
        this.userRepository=userRepository;
        this.applicationRepository=applicationRepository;
    }

    @GetMapping("/browseJobOffers")
    public String browseJobOffers(Model model)
    {
        List<Joboffers> jobOffers = jobOfferRepository.findAllByStatus(Joboffers.JobOfferStatus.ACCEPTED);
        model.addAttribute("jobOffers", jobOffers);
        return "job_offer_list"; // Return the HTML template name to be rendered
    }

    @GetMapping("/jobOfferDetails/{id}")
    public String showJobOfferDetails(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Joboffers jobOffer = jobOfferRepository.findById(id).orElse(null);
        String username = userDetails.getUsername();
        if (jobOffer == null) {
            // Job offer with the given id was not found. Handle the error here (e.g., redirect to an error page).
            return "error"; // You can create an "error.html" template for handling such cases.
        }
        model.addAttribute("jobOffer", jobOffer);

        Optional<Joboffers> jobOfferOptional = jobOfferRepository.findById(id);
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (jobOfferOptional.isPresent() && userOptional.isPresent()) {
            Joboffers jobOffers = jobOfferOptional.get();
            User user = userOptional.get();
            Applications applicationExists = applicationRepository.findByJoboffersIdAndUserId(jobOffers.getId(), user.getId());
            boolean alreadyApplied = false;
            if (applicationExists != null) {
                alreadyApplied = true;
                model.addAttribute("alreadyApplied", alreadyApplied);
            } else {
                model.addAttribute("alreadyApplied", alreadyApplied);
            }
        }
        return "job_offer_details"; // Return the HTML template name to be rendered
    }

    @GetMapping("/applyJobOffer/{id}")
    public String applyJobOffer(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        LocalDate dateApplied = java.time.LocalDate.now();
        String username = userDetails.getUsername();
        Optional<Joboffers> jobOfferOptional = jobOfferRepository.findById(Integer.parseInt(id));
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (jobOfferOptional.isPresent() && userOptional.isPresent()) {
            Joboffers jobOffer = jobOfferOptional.get();
            User user = userOptional.get();
            Applications application = new Applications(Applications.ApplicationStatus.INPROGRESS, user, jobOffer, dateApplied);
            applicationRepository.save(application);
        }
        return "redirect:/applicant/browseJobOffers";
    }

}
