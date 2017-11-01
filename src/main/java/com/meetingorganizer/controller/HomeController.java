package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home page controller
 * @author Aleksander
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    public static final String HOME_PAGE = "homePage";

    @GetMapping
    public String displayHomePage() {
        return HOME_PAGE;
    }
}
