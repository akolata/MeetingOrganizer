package com.meetingorganizer.controller;

import com.meetingorganizer.dto.RegistrationFormDto;
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

    private ValidationErrorMessagesUtils errorsUtils;

    public final static String REGISTRATION_PAGE = "register";

    @Autowired
    public RegistrationController(ValidationErrorMessagesUtils errorsUtils) {
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

        return REGISTRATION_PAGE;
    }


}
