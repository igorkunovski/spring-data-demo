package com.example.secutity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .authorizeHttpRequests(registry -> registry

                        .requestMatchers("/ui/issuetable/**").hasAnyAuthority(Role.ADMIN.name(), Role.DEVELOPER.name())
                        .requestMatchers("/ui/readerlist/**").hasAnyAuthority(Role.READER.name(), Role.ADMIN.name(), Role.DEVELOPER.name())
                        .requestMatchers("/ui/booklist/**").permitAll()

                        .requestMatchers("/ui/**").hasAuthority(Role.DEVELOPER.name())
                        .anyRequest().permitAll()
                )
                .formLogin(Customizer.withDefaults())
                .build();
    }
}
