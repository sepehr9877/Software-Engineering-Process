package ca.concordia.soen6011.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;

import ca.concordia.soen6011.model.User;

public interface UserService {

	@Async
	public CompletableFuture<Optional<User>> findUser(String username);

	@Async
	public CompletableFuture<List<User>> findUserByName(String name);	
}
