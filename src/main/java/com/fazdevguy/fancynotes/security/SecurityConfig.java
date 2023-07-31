package com.fazdevguy.fancynotes.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {


    // Support for JDBC
    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // query to retrieve user by username
        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, active from users where username =?");

        // query to retrieve roles/authorities by username
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from roles where username = ?");

        return jdbcUserDetailsManager;

    }


    // Filter Chain is a tool to handle protection of every endpoint
    // and also to set login/register/error pages
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(configurer -> configurer.
                        requestMatchers("/register/**").permitAll()
                        .requestMatchers("/categories/**").hasRole("EMPLOYEE")
                .requestMatchers("/").hasRole("EMPLOYEE")
                .anyRequest().authenticated()
        )
               .formLogin(form ->
                       form
                                .loginPage("/login/loginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer -> configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }

}
