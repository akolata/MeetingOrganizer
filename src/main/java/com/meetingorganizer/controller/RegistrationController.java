package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.events.RegistrationCompleteEvent;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.ValidationErrorMessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

/**
 * Controller for registration page
 * @author Aleksander
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    public final static String  REGISTRATION_PAGE = "register",
                                REDIRECT_TO_LOGIN_PAGE = "redirect:/login",
                                REGISTRATION_CONFIRM_ENDPOINT = "/confirm";

    private UserService userService;
    private ValidationErrorMessagesUtils errorsUtils;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserService userService, ValidationErrorMessagesUtils errorsUtils,
                                  ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.errorsUtils = errorsUtils;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public String displayRegistrationPage(Model model){
        model.addAttribute("dto", new RegistrationFormDto());
        return REGISTRATION_PAGE;
    }

    @PostMapping
    public String processRegistrationForm(@Valid @ModelAttribute(name = "dto") RegistrationFormDto dto,
                                          BindingResult bindingResult, Model model,
                                          RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                model.addAllAttributes(
                        errorsUtils.errorMessagesForClassLevelValidations(bindingResult.getGlobalErrors()));
            }

            return REGISTRATION_PAGE;
        }

        if (userService.isEmailAlreadyTaken(dto.getEmail())) {
            model.addAttribute("emailAlreadyTaken", Boolean.TRUE);
            return REGISTRATION_PAGE;
        }

        User registeredUser = userService.saveRegisteredUser(dto);
        String applicationUri = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

        try{
            eventPublisher.publishEvent(new RegistrationCompleteEvent(registeredUser, applicationUri,
                    LocaleContextHolder.getLocale()));

            redirectAttributes.addFlashAttribute("registrationSuccessful", Boolean.TRUE);
        }catch (Exception e){
            model.addAttribute("emailSendException", Boolean.TRUE);
            return REGISTRATION_PAGE;
        }

        return REDIRECT_TO_LOGIN_PAGE;
    }

    @GetMapping("/confirm")
    public String confirmRegistration(@RequestParam("token") String token, RedirectAttributes redirectAttributes){

        VerificationToken verificationToken = userService.getVerificationToken(token);

        if(verificationToken == null){
            redirectAttributes.addFlashAttribute("tokenNotFound", Boolean.TRUE);
            return REDIRECT_TO_LOGIN_PAGE;
        }

        if(verificationToken.isTokenExpired()){
            redirectAttributes.addFlashAttribute("tokenExpired", Boolean.TRUE);
            return REDIRECT_TO_LOGIN_PAGE;
        }

        User userForToken = verificationToken.getUser();
        userForToken.setEnabled(true);
        userService.saveUser(userForToken);
        redirectAttributes.addFlashAttribute("userEnabled", Boolean.TRUE);

        return REDIRECT_TO_LOGIN_PAGE;
    }

}
