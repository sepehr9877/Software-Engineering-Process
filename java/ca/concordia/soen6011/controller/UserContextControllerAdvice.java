package ca.concordia.soen6011.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


@ControllerAdvice
public class UserContextControllerAdvice {
	
	@ModelAttribute("currentUser")
    public String getCurrentUser(Authentication authentication) {
		String username="";
		if(authentication!=null)
			username=authentication.getName();
		return username;
        
    }
	@ModelAttribute("ROLE_ADMIN")
	public boolean getCurrentRole(Authentication authentication) {
		if(authentication!=null) {
		for (GrantedAuthority authority : authentication.getAuthorities()) {
            String roleName = authority.getAuthority();
            if ("ROLE_ADMIN".equals(roleName)) {
                return true;
            }
        }}
		return false;
	}

}
