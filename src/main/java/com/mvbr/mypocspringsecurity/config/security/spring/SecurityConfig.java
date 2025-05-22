package com.mvbr.mypocspringsecurity.config.security.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/*
Allows restricting access based upon the HttpServletRequest using RequestMatcher
implementations (i.e. via URL patterns).
Example Configurations
The most basic example is to configure all URLs to require the role "ROLE_USER".
The configuration below requires authentication to every URL and will grant access to
both the user "admin" and "user".
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Todas as requisições precisam estar autenticadas...
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated())
                // Ativa o formulário de login padrão...
                .formLogin(withDefaults())
                // Ativa autenticação HTTP Basic (útil para testar com tools tipo Postman)...
                .httpBasic(withDefaults());

        return httpSecurity.build();

    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

        final String HOLE_USER = "user";
        final String HOLE_ADMIN = "admin";

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles(HOLE_USER)
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("password"))
                .roles(HOLE_USER, HOLE_ADMIN)
                .build();

        return new InMemoryUserDetailsManager(user, admin);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

//We can also configure multiple URLs. The configuration below requires authentication to every URL and
// will grant access to URLs starting with /admin/ to only the "admin" user. All other URLs either user can access.
//@Configuration
//@EnableWebSecurity
//public class AuthorizeUrlsSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/**").hasRole("USER")
//                )
//                .formLogin(withDefaults());
//        return http.build();
//    }
//

//Note that the matchers are considered in order. Therefore, the following is invalid because the
// first matcher matches every request and will never get to the second mapping:
//@Configuration
//@EnableWebSecurity
//public class AuthorizeUrlsSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .requestMatchers("/**").hasRole("USER")
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                );
//        return http.build();
//    }
//}
//















