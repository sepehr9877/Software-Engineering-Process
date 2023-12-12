package ca.concordia.soen6011.controller;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 *Controller in charge of displaying and updating account info for the user.
 * If user tries to access the account page of another user, it will be redirected to
 * home page.
 * @author Soroor Seyed Aghamiri
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    UserRepository userRepository;

    /**
     * If user is requesting to see their own page, shows their detail.
     * Else redirects user to home page.
     * @param username username received in the URL
     * @param userDetails to get authenticated info
     * @param model HTML model
     * @return
     */
    @GetMapping("/{username}")
    public String goToMyAccount(@PathVariable("username")String username,
                                @AuthenticationPrincipal UserDetails userDetails, Model model){
        if(!username.equals(userDetails.getUsername())){
            return "redirect:/";
        }
        Optional<User> thisUser = userRepository.findByUsername(username);
        if(thisUser.isPresent()){
            User existing = thisUser.get();
            String fullname = existing.getFirstname()+" "+existing.getLastname();
            String firstname = existing.getFirstname();
            String lastname = existing.getLastname();
            String email = existing.getEmail();
            model.addAttribute("authname", userDetails.getUsername());
            model.addAttribute("user", existing);
            model.addAttribute("myFullName",fullname);
            model.addAttribute("username",username);
            model.addAttribute("fistname",firstname);
            model.addAttribute("lastname", lastname);
            model.addAttribute("email", email);
        }
        return "my_account";
    }

    /**
     * User can update their info. If any information is changed, based on user's role,
     * <code>updatecompany</code> or <code>updateCandidate</code> is called.
     * @param firstName value in the first name field
     * @param lastName value in the last name field
     * @param email value in the email field
     * @param userDetails to get authenticated info
     * @return
     */
    @PostMapping("/updateMyInfo")
    public String updateInfo(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("email") String email,
                             @AuthenticationPrincipal UserDetails userDetails){
        try{
            boolean mustUpdateInfo = false;
            String userName = userDetails.getUsername();
            Optional<User> user = userRepository.findByUsername(userName);
            if (user.isPresent()) {
                User existingU = user.get();
                if (!existingU.getFirstname().equals(firstName)
                        || !existingU.getLastname().equals(lastName) || !existingU.getEmail().equals(email))
                    mustUpdateInfo = true;
                if (mustUpdateInfo) {
                    updateUser(userName, email, firstName, lastName);
                }
            }
            return "redirect:/account/" + userName;
        }catch(Exception e){
            return "error";
        }
    }

    private void updateUser(String username, String email, String firstName, String lastName){

    	Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()){
            User u = user.get();
            u.setEmail(email);
            u.setFirstname(firstName);
            u.setLastname(lastName);
            userRepository.save(u);
        }
    }
}
