package com.noice.userbff.service.impl;

import com.noice.userbff.dto.UserDto;
import com.noice.userbff.entity.User;
import com.noice.userbff.enums.RoleType;
import com.noice.userbff.repository.UserRepository;
import com.noice.userbff.security.JWTConfig;
import com.noice.userbff.service.repo.AccountServices;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountServices {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private JWTConfig jwtConfig;


    @Override
    public void registerUser(UserDto userDto) {
        User.UserBuilder userBuilder = User.builder();

        userBuilder.firstName(userDto.firstName())
            .lastName(userDto.lastName())
            .email(userDto.email())
            .password(passwordEncoder.encode(userDto.password()))
                .role(RoleType.ROLE_CUSTOMER)
                .phoneNumber(userDto.phoneNumber());

        userRepository.save(userBuilder.build());
    }


    @Override
    public String authenticate(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        return jwtConfig.generateToken(authentication);
    }
}
