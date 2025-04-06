package com.indpro.task_manager.service;

import com.indpro.task_manager.Request.LoginDetails;
import com.indpro.task_manager.Request.VerifyEmailRequest;
import com.indpro.task_manager.entity.User;

public interface IUserService {

    String registerHere(User registration);

    String login(LoginDetails loginDetails);

    String generateToken(String email);

    String verifyToken(VerifyEmailRequest verifyEmailRequest);

    String verifyFpToken(String fpToken,String newPassword);

    String generateFpToken(String email);
}
