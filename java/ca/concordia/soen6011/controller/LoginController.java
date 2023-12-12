package ca.concordia.soen6011.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

/**
 * This is the Controller class for Login. Users enter Login page and enter their credentials.
 * If the information provided is valid, the user is redirected to their profile page,
 * otherwise, they are asked to re-enter their information.
 * @author Soroor Seyed Aghamiri
 */
@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    /**
     * Open login page.
     * @return login page URL
     */
    @GetMapping("/login")
    public String openLoginPage(){ return "new_login_page"; }

    @GetMapping("/login-success")
    public String loginsuccess(Authentication authentication) {
    	String username=authentication.getName();
    	return "redirect:/profile/" + username;
    }

    @GetMapping("/forgot-password")
    public String openForgotPassword(){
        return "forgot_password";
    }

    @PostMapping("/sendEmail")
    public String checkEmail(@RequestParam("email") String email,Model model){
        Optional<User> foundUser = userRepository.findByEmail(email);
        if(foundUser.isPresent()){
            model.addAttribute("userEmail", email);
            model.addAttribute("enableForm", true);
        }else{
            model.addAttribute("param.error", true);
        }
        return "forgot_password";
    }

    @PostMapping("/updatePass")
    public String updatePassword(@RequestParam("userEmail") String email,@RequestParam("password") String password){
        Optional<User> tbu = userRepository.findByEmail(email);
        if(tbu.isPresent()){
            User u = tbu.get();
            u.setPassword(password);
            userRepository.save(u);
        }
        return "new_login_page";
    }
}


