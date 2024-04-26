package com.giloguy.examcs.controllers;

import java.net.URI;
import java.util.Collections;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.giloguy.examcs.payloads.UserLogin;
import com.giloguy.examcs.payloads.UserSignUp;
import com.giloguy.examcs.payloads.JwtAuthenticationResponse;
import jakarta.validation.Valid;
import com.giloguy.examcs.models.Roles;
import com.giloguy.examcs.models.RoleName;
import com.giloguy.examcs.models.Users;
import com.giloguy.examcs.repositories.UserRepository;
import com.giloguy.examcs.repositories.RoleRepository;
import com.giloguy.examcs.security.JWTTokenProvider;

@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserSignUp signUp) {
        if (userRepository.existsByEmail(signUp.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Users user = new Users(
                signUp.getName(),
                signUp.getEmail(),
                signUp.getPassword()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Roles userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("User Role not set"));

        user.setRoles(Collections.singleton(userRole));

        Users result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/users/{id}")
                .buildAndExpand(result.getId())
                .toUri();

        return ResponseEntity.created(location).body("User registered successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody UserLogin login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getEmail(),
                        login.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
