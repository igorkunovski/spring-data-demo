package com.example.secutity;

import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                List.of(new SimpleGrantedAuthority((user.getRole().name()))
        ));
    }
}
