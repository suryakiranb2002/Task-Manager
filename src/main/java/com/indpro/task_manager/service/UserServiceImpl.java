package com.indpro.task_manager.service;

import com.indpro.task_manager.Request.LoginDetails;
import com.indpro.task_manager.Request.VerifyEmailRequest;
import com.indpro.task_manager.entity.*;
import com.indpro.task_manager.repository.ConfirmationTokenRepository;
import com.indpro.task_manager.repository.FpConfirmationTokenRepository;
import com.indpro.task_manager.repository.JwtTokenRepository;
import com.indpro.task_manager.repository.UserRepository;
import com.indpro.task_manager.security.JwtService;
import com.indpro.task_manager.wrapper.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private FpConfirmationTokenRepository fpConfirmationTokenRepository;
    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Override
    public String registerHere(User user) {
        if(userRepository.existsByEmail(user.getEmail())){
            return "Email already exists";
        }
        var user1=User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .role(Role.USER)
                .enabled(false)
                .build();
        userRepository.save(user1);
        String token = generateToken(user1.getEmail());
        return "Registered Successfully and here is the verification token : " + token;
    }

    @Override
    public String login(LoginDetails loginDetails) {

        if(userRepository.findByEmail(loginDetails.getEmail())==null){
            return "User did not Register";
        }
        else {
            User user = userRepository.findByEmail(loginDetails.getEmail());
            if (user.getEnabled()) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword()));
                var jwtToken = jwtService.generateToken(new CustomUserDetails(user));
                saveUserJwtToken(user, jwtToken);
                return jwtToken;
            } else {
                return "Email not verified";
            }
        }
    }
    @Override
    public String generateToken(String email) {
        if(userRepository.findByEmail(email) ==null){
            return "Invalid Email. Register if you didn't register before";
        }
        else {
            String token = UUID.randomUUID().toString();
            User user = userRepository.findByEmail(email);
            ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
            confirmationTokenRepository.save(confirmationToken);
            return token;
        }
    }
    @Override
    public String verifyToken(VerifyEmailRequest verifyEmailRequest) {
        if(confirmationTokenRepository.findByToken(verifyEmailRequest.getToken()) ==null){
            return "Invalid Token";
        }
        else {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(verifyEmailRequest.getToken());
            User user = confirmationToken.getUser();
            if (!LocalDateTime.now().isBefore(confirmationToken.getExpiresAt())) {
                return "Token got Expired. Click on resend to generate new token";
            }
            else {
                user.setEnabled(true);
                userRepository.save(user);
                return "Verification Successful.";
            }
        }
    }
    @Override
    public String verifyFpToken(String fpToken,String newPassword) {
        if(fpConfirmationTokenRepository.findByFpToken(fpToken)==null){
            return "Invalid Token";
        }
        else {
            FpConfirmationToken fpConfirmationToken = fpConfirmationTokenRepository.findByFpToken(fpToken);
            User user = fpConfirmationToken.getUser();
            if (!LocalDateTime.now().isBefore(fpConfirmationToken.getExpiresAt())) {
                return "Token got Expired. Click on resend to generate new token";
            } else {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return "Changed Password Successfully";
            }
        }
    }
    @Override
    public String generateFpToken(String email) {
        if(userRepository.findByEmail(email)==null){
            return "Invalid Email. Register if you didn't register before";
        }
        String fpToken = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(email);
        FpConfirmationToken fpConfirmationToken = new FpConfirmationToken(fpToken, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        fpConfirmationTokenRepository.save(fpConfirmationToken);
        return fpToken;
    }
    public void saveUserJwtToken(User user, String jwtToken) {
        JwtToken jwtToken1=new JwtToken(jwtToken,false,false,user);
        jwtTokenRepository.save(jwtToken1);
    }

}
