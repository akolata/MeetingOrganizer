package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Aleksander on 17.10.2017.
 * Controller for Login page
 */
@Controller
public class LoginController {

    private static final String LOGIN_PAGE = "login";

    @GetMapping("/")
    public String redirectToLogin(){
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String displayLoginPage(){
        return LOGIN_PAGE;
    }
}
