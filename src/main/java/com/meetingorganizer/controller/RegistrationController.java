package com.meetingorganizer.controller;

import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.ValidationErrorMessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Controller for registration page
 * @author Aleksander
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    public final static String REGISTRATION_PAGE = "register",
            REDIRECT_TO_SUCCESS_LOGIN_PAGE = "redirect:/login";

    private UserService userService;
    private ValidationErrorMessagesUtils errorsUtils;

    @Autowired
    public RegistrationController(UserService userService, ValidationErrorMessagesUtils errorsUtils) {
        this.userService = userService;
        this.errorsUtils = errorsUtils;
    }

    @GetMapping
    public String displayRegistrationPage(Model model){
        model.addAttribute("dto", new RegistrationFormDto());
        return REGISTRATION_PAGE;
    }

    @PostMapping
    public String processRegistrationForm(@Valid @ModelAttribute(name = "dto") RegistrationFormDto dto,
                                          BindingResult bindingResult,
                                          Model model) {

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                model.addAllAttributes(errorsUtils.errorMessagesForClassLevelValidations(bindingResult.getGlobalErrors()));
            }

            return REGISTRATION_PAGE;
        }

        if (userService.isEmailAlreadyTaken(dto.getEmail())) {
            model.addAttribute("emailAlreadyTaken", Boolean.TRUE);
            return REGISTRATION_PAGE;
        }

        userService.saveUser(dto);

        return REDIRECT_TO_SUCCESS_LOGIN_PAGE;
    }


}
