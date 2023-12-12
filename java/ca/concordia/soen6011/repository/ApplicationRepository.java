package ca.concordia.soen6011.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.concordia.soen6011.model.Applications;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.model.Applications.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Applications,Integer> {

	public List<Applications> findAllByStatus(ApplicationStatus status);
	public List<Applications> findByJoboffersId(Long jobOffersId);
	public List<Applications> findByUser(User user);
	public Applications findByJoboffersIdAndUserId(int jobOffersId, int userId);

	public List<Applications> findByUserUsername(String username);	
	public Optional<Applications> findByUserUsernameAndId(String username, int id);	
}
