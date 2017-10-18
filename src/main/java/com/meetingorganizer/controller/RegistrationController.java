package com.meetingorganizer.controller;

import com.meetingorganizer.dto.RegistrationFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Aleksander on 17.10.2017.
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private final static String REGISTRATION_PAGE = "register";

    @GetMapping
    public String displayRegistrationPage(Model model){
        model.addAttribute("dto", new RegistrationFormDto());
        return REGISTRATION_PAGE;
    }
}
