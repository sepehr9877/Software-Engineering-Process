package ca.concordia.soen6011.service;

import org.springframework.stereotype.Service;

import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.repository.JobOfferRepository;

@Service
public class AdminService {

	private final JobOfferRepository jobOfferRepository;
	
	public AdminService(JobOfferRepository jobOfferRepository) {
		this.jobOfferRepository=jobOfferRepository;
	}
	public Joboffers getJobOfferById(int id) {
		
		return this.jobOfferRepository.findById(id).get();
	}
}
