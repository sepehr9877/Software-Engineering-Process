package ca.concordia.soen6011;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableAsync
public class Soen6011Application {
	
	public static void main(String[] args) {
		
		SpringApplication.run(Soen6011Application.class, args);
	}

	@Bean
	Executor taskExecutor() {
    
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
    	executor.setQueueCapacity(500);
    	executor.setThreadNamePrefix("GithubLookup-");
    	executor.initialize();
    	return executor;
  }
	
}
