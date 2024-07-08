package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addSignedInUserInformation(Model model, Principal principal){
        if(principal == null){
            return;
        }
        System.out.println("adding signedin user info to model");
        String username = principal.getName();
        logger.info("User Signed in:{}",username);
        User user = userService.getUserByEmail(username);
            System.out.println(user);
            System.out.println(user.getName());
            System.out.println(user.getEmail());
            model.addAttribute("signedInUser", user);
        
    }
}
