package ca.concordia.soen6011.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ca.concordia.soen6011.model.Joboffers;
import ca.concordia.soen6011.model.Joboffers.JobOfferStatus;

public interface JobOfferRepository extends JpaRepository<Joboffers,Integer>  {

	public List<Joboffers> findAllByStatus(JobOfferStatus status);
	public List<Joboffers> findByTitleContainingIgnoreCase(String title);
  	public List<Joboffers> findByCompanyId(Integer companyId);
	public List<Joboffers> findByCompanyName(String companyName);

	@Query("SELECT DISTINCT companyName FROM Joboffers")
	List<String> findDistinctCompanies();

}

