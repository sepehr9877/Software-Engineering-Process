package ca.concordia.soen6011.repository;

import java.util.List;
import java.util.Optional;

import ca.concordia.soen6011.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


/**
 * User Interface to query User table.
 * @author Soroor Seyed Aghamiri
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * Queries User table to find the user based on username and password.
     * @param username username received from the view.
     * @param password password received from the view.
     * @return User object
     */
	@Query("SELECT u FROM User u WHERE u.username <> :username")
    List<User> findAllExceptUsername(String username);
    User findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);

    /**
     * Queries User dataset and retrieves a user, if available.
     * @param username username of the queried user
     * @return a user, if available
     */
    Optional<User> findByUsername(String username);

    List<User> findByIsActiveFalse();
    Optional<User> findById(int id);

    /**
     * Queries User dataset and retrieves a user matching the text
     * @param firstname match on firstname
     * @param lastname match on lastname
     * @return a user, if available
     */
	public List<User> findByFirstnameOrLastnameContainingIgnoreCase(String firstname, String lastname);

    Optional<User> findByEmail(String email);
}

