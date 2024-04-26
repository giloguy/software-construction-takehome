package com.giloguy.examcs.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.giloguy.examcs.models.Users;
import com.giloguy.examcs.payloads.UserSummary;
import com.giloguy.examcs.services.Userservice;
import com.giloguy.examcs.security.CurrentUser;
import com.giloguy.examcs.security.UserPrincipal;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private Userservice userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserSummary> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        List<UserSummary> userSummaryList;

        userSummaryList = users.stream()
                .map(user -> new UserSummary(user.getId(), user.getName(), user.getEmail()))
                .toList();

        return userSummaryList;
    }

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getName(), currentUser.getEmail());
    }
}
