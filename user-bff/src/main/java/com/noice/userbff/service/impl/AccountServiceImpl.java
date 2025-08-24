package com.noice.userbff.service.impl;

import com.noice.userbff.dto.UserDto;
import com.noice.userbff.entity.User;
import com.noice.userbff.enums.RoleType;
import com.noice.userbff.exception.NoiceBusinessLogicException;
import com.noice.userbff.projection.UserProfileProjection;
import com.noice.userbff.repository.UserRepository;
import com.noice.userbff.security.JWTConfig;
import com.noice.userbff.service.repo.AccountServices;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public void updateUser(UserDto userDto, Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new NoiceBusinessLogicException("User not found"));

        u.setFirstName(userDto.firstName());
        u.setLastName(userDto.lastName());
        u.setEmail(userDto.email());
        u.setPhoneNumber(userDto.phoneNumber());
    }

    @Override
    public UserProfileProjection getProfile(Long id) {
         return userRepository.findBy((root, q, cb) -> cb.equal(root.get("id"), id), q-> q.as(UserProfileProjection.class).first()).orElseThrow( () -> new NoiceBusinessLogicException("no user found"));
    }
}
