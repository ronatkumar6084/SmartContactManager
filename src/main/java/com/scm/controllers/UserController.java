package com.scm.controllers;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    //user dashboard page
    //@GetMapping("/dashboard")
    @RequestMapping(value="/dashboard")
    public String userDashboard()
    {
        System.out.println("User Dashboard");
        return "user/dashboard";
    }

    //user profile page
    //@GetMapping("/profile")
    @RequestMapping(value="/profile")
    public String userProfile()
    {
        System.out.println("User Profile");
        return "user/profile";
    }
    //user add contact page

    //user view contact

    //user edit contact

    //user delete contact

    //user search contact
}