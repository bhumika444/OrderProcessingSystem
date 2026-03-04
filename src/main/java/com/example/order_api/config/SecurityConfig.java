package com.example.order_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        // API demo: disable CSRF so curl/Postman POST works
        .csrf(csrf -> csrf.disable())

        // Allow H2 console to render in a frame
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

        .authorizeHttpRequests(auth -> auth
            // allow H2 console
            .requestMatchers("/h2-console/**").permitAll()

            // allow GETs for orders without auth
            .requestMatchers(HttpMethod.GET, "/orders/**").permitAll()

            // protect POST /orders
            .requestMatchers(HttpMethod.POST, "/orders").authenticated()

            // everything else open (optional)
            .anyRequest().permitAll()
        )

        // use Basic auth for demo
        .httpBasic(Customizer.withDefaults())
        .build();
}

  @Bean
  public UserDetailsService users() {
    UserDetails user = User.withUsername("admin")
      .password("{noop}admin123")
      .roles("USER")
      .build();
    return new InMemoryUserDetailsManager(user);
  }
}