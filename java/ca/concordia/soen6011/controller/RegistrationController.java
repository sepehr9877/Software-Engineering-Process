package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.Role;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Registration controller is responsible for registering companys and users. After user fills the
 * form, the information is saved in the company or user table based on the role they choose.
 * @author Soroor Seyed Aghamiri
 */
@Controller
public class RegistrationController {
	
    @Autowired
    public UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
  

    /**
     * Opens the registration page.
     * @return
     */
    @GetMapping("/register")
    public String OpenRegisterPage(){
        return "registration_page"; }

    /**
     * Based on selected role, saves the info into user or company table.
     * @param userName string, username entered by user
     * @param email string, email address entered by user
     * @param firstName string, first name entered by user
     * @param lastName string, last name entered by user
     * @param password string, password entered by user
     * @return view to be redirected to
     * @throws ServletException
     */
    @PostMapping("/register")
    public ModelAndView register(@RequestParam("userName") String userName,
                              @RequestParam("email") String email,
                              @RequestParam("firstName") String firstName,
                              @RequestParam("lastName") String lastName,
                              @RequestParam("password") String password
            ,HttpServletRequest request) throws ServletException {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration_page");
        registerUser(userName, email, firstName, lastName, password);


        Optional<User> userOptional = userRepository.findByUsername(userName);
        if (userOptional.isPresent()) {
        	
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);
            authToken.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticated = authenticationManager.authenticate(authToken);
            request.login(userName, password);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        }
        
        modelAndView.setViewName("redirect:/login");
        return modelAndView;
    }


    /**
     * Adds the info as a new record to the User table.
     * @param username
     * @param email
     * @param firstName
     * @param lastName
     * @param password
     */
    private void registerUser(String username, String email, String firstName, String lastName,
                                  String password){
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstname(firstName);
        user.setLastname(lastName);

        user.setPassword(password);
        Role role = new Role();
        role.setName("USER");
        role.setUser(user);
        List<Role> allRoles = new ArrayList<Role>();
        allRoles.add(role);
        user.setRoles(allRoles);
        this.userRepository.save(user);
    }

    /**
     * Gets request from registration_page to check if the username exists in database.
     * @param username the username to be checked for availability
     * @return
     */

    @GetMapping("/checkUsername")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        // Check if the username exists in the database
        boolean usernameExists = userRepository.existsByUsername(username);

        // Return the result with the appropriate status code
        if (usernameExists) {
            return ResponseEntity.ok(true); // Username exists
        } else {
            return ResponseEntity.ok(false); // Username doesn't exist
        }
    }

}
