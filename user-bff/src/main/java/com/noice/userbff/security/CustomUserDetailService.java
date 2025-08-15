package com.noice.userbff.security;

import com.noice.userbff.entity.User;
import com.noice.userbff.repository.UserRepository;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String search) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCaseOrPhoneNumber(search).orElseThrow(() -> new UsernameNotFoundException(search));
      return new CustomUserDetail(user);
    }
}
