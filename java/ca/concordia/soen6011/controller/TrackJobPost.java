package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.ApplicationRepository;
import ca.concordia.soen6011.repository.JobOfferRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class TrackJobPost {
    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/trackJobPost")
    public String viewByCompany(@RequestParam(value = "company", required = false) String company, Model model) {
        List<String> companies = jobOfferRepository.findDistinctCompanies();
        model.addAttribute("companies", companies);

        if (company != null && !company.isEmpty()) {
            List<Joboffers> jobOffers = jobOfferRepository.findByCompanyName(company);
            model.addAttribute("jobOffers", jobOffers);
        }

        return "track-job-post";
    }

    @GetMapping("/viewApplicants")
    public String viewApplicants(@RequestParam("jobOfferId") Long jobOfferId, Model model) {
        List<Applications> applications = applicationRepository.findByJoboffersId(jobOfferId);
        
        model.addAttribute("applications", applications);
        return "view-applicants";
    }


//    @GetMapping("/viewJobOffersByEmployer/{employerId}")
//    public String viewJobOffersByEmployer(@PathVariable Integer employerId, Model model) {
//        List<Joboffers> jobOffers = jobOfferRepository.findByEmployerId(employerId);
//        model.addAttribute("jobOffers", jobOffers);
//        return "track-job-post.html-by-employer-id"; // This should be the name of your Thymeleaf template for viewing job offers
//    }

}
