package com.indpro.task_manager.controller;

import com.indpro.task_manager.Request.*;
import com.indpro.task_manager.entity.User;
import com.indpro.task_manager.service.IUserService;
import com.indpro.task_manager.wrapper.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;
    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        UserProfileDTO profile = new UserProfileDTO(
                userDetails.getUserEntity().getId(),
                userDetails.getUserEntity().getFirstName(),
                userDetails.getUserEntity().getEmail()
        );

        return ResponseEntity.ok(profile);
    }


    @PostMapping("/api/v1/auth/login")
    public String login(@RequestBody LoginDetails loginDetails) {
        return userService.login(loginDetails);
    }

    @PostMapping("/api/v1/auth/registration")
    public String registerHere(@RequestBody User user) {
        return userService.registerHere(user);
    }

    @PostMapping("/api/v1/auth/resendToken")
    public String resendToken(@RequestBody ResendTokenRequest resendTokenRequest) {
        return userService.generateToken(resendTokenRequest.getEmail());
    }

    @PostMapping("/api/v1/auth/confirm")
    public String getToken(@RequestBody VerifyEmailRequest verifyEmailRequest) {
        return userService.verifyToken(verifyEmailRequest);
    }

    @PostMapping("/api/v1/auth/sendFpToken")
    public String sendFpToken(@RequestBody ResendTokenRequest resendTokenRequest){
        return userService.generateFpToken(resendTokenRequest.getEmail());
    }

    @PostMapping("/api/v1/auth/forgot-password")
    public String getFpToken(@RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return userService.verifyFpToken(forgotPasswordRequest.getFpToken(),forgotPasswordRequest.getNewPassword());
    }
}