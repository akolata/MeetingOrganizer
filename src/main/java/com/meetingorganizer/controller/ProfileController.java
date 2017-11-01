package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Profile's page controller
 * @author Aleksander
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    public static final String PROFILE_PAGE = "profile";

    @GetMapping
    public String displayProfilePage(Authentication authentication, Model model) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("dto", currentUser);

        return PROFILE_PAGE;
    }
}
