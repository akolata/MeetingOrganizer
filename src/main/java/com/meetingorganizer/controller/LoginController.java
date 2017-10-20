package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for Login page
 * @author Aleksander
 */
@Controller
public class LoginController {

    public static final String LOGIN_PAGE = "login";

    @GetMapping("/")
    public String redirectToLogin(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String displayLoginPage(){
        return LOGIN_PAGE;
    }
}
