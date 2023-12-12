package ca.concordia.soen6011.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<ca.concordia.soen6011.model.User> userOptional = this.userRepository.findByUsername(username);
		if(userOptional.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}
		
		User user = userOptional.get();
		return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
			.password(user.getPassword())
			.disabled(!user.getIsActive())
			.roles(user.getRoleArray())
			.build();
	}

}
