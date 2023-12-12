package ca.concordia.soen6011.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig  {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
        http
            .authorizeHttpRequests(requests -> requests
            		.requestMatchers("/", "/error", "/register").permitAll()

                    .requestMatchers("/checkUsername","/company/submitinterview").permitAll()

                    .requestMatchers("/checkUsername").permitAll()
                    .requestMatchers("/account/updateMyInfo").permitAll()
                    .requestMatchers("/forgot-password").permitAll()
                    .requestMatchers("/updatePass").permitAll()
                    .requestMatchers("/sendEmail").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
            		.anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").defaultSuccessUrl("/login-success")
                .permitAll()
            ).logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true).clearAuthentication(true)
                  .permitAll());
        
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConfiguration) throws Exception {
      return authConfiguration.getAuthenticationManager();
    }

}
