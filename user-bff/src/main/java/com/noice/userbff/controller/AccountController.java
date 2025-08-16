package com.noice.userbff.controller;

import com.noice.userbff.dto.LoginDto;
import com.noice.userbff.dto.UserDto;
import com.noice.userbff.service.repo.AccountServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountServices accountServices;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }


    @PostMapping("/signin")
    public String authenticate(@RequestBody LoginDto loginDto) {
        return accountServices.authenticate(loginDto.username(),loginDto.password());
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto userDto){
         accountServices.registerUser(userDto);
         return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
