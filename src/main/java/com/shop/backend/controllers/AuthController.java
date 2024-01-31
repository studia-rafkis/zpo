package com.shop.backend.controllers;
import com.shop.backend.config.UserAuthProvider;
import com.shop.backend.dtos.*;
import com.shop.backend.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final UserService userService;
    private final UserAuthProvider userAuthenticationProvider;
    @GetMapping("/user/{email}")
    public ResponseEntity<UserDto> getUser(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        System.out.println(userDto);
        return ResponseEntity.ok(userDto);
    }
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }
    @PutMapping("/user/{email}")
    public ResponseEntity<UserDto> update(@PathVariable String email, @RequestBody @Valid UserDto updatedUserDto) {
        UserDto updatedUser = userService.updateUser(email, updatedUserDto);
        return ResponseEntity.ok(updatedUser);
    }
    @PostMapping("/user/checkOldPassword")
    public ResponseEntity<?> checkOldPassword(@RequestBody PasswordRequest request) {
        boolean success = userService.checkOldPassword(request.getEmail(), request.getOldPassword());
        return ResponseEntity.ok(new PasswordResponse(success));
    }
    @PostMapping("/user/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequest request) {
        boolean success = userService.updatePassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(new PasswordResponse(success));
    }
}
