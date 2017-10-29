package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by Aleksander on 29.10.2017.
 */
@Controller("/home")
public class HomeController {

    private static final String HOME_PAGE = "home";

    @GetMapping
    public String displayHomePage() {
        return HOME_PAGE;
    }
}
