package ca.concordia.soen6011.service;

import ca.concordia.soen6011.model.User;
import ca.concordia.soen6011.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		
		this.userRepository = userRepository;
	}
	
	@Async
	public CompletableFuture<Optional<User>> findUser(String username) {

		return CompletableFuture.supplyAsync(() -> 

			userRepository.findByUsername(username)
		);
	}
	
	@Async
	public CompletableFuture<List<User>> findUserByName(String name) {
	
		return CompletableFuture.supplyAsync(() -> 
		
			userRepository.findByFirstnameOrLastnameContainingIgnoreCase(name, name)
		);	
	}
}
