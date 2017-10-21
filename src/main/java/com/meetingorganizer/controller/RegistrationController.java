package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.domain.VerificationToken;
import com.meetingorganizer.dto.RegistrationFormDto;
import com.meetingorganizer.dto.ResendTokenDto;
import com.meetingorganizer.events.RegistrationCompleteEvent;
import com.meetingorganizer.service.MailService;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.ValidationErrorMessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.UnknownHostException;

/**
 * Controller for registration page
 * @author Aleksander
 */
@Controller
@RequestMapping("/register")
public class RegistrationController {

    public final static String  REGISTRATION_PAGE = "register",
                                REDIRECT_TO_LOGIN_PAGE = "redirect:/login",
                                REGISTRATION_CONFIRM_ENDPOINT = "/confirm",
                                RESEND_TOKEN_PAGE = "resendToken";

    private UserService userService;
    private MailService mailService;
    private ValidationErrorMessagesUtils errorsUtils;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public RegistrationController(UserService userService,
                                  MailService mailService,
                                  ValidationErrorMessagesUtils errorsUtils,
                                  ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.mailService = mailService;
        this.errorsUtils = errorsUtils;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping
    public String displayRegistrationPage(Model model){
        model.addAttribute("dto", new RegistrationFormDto());
        return REGISTRATION_PAGE;
    }

    /**
     * Register new user if form has no errors and send email with authorization token
     * @param dto form's dto
     * @param model
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping
    public String processRegistrationForm(@Valid @ModelAttribute(name = "dto") RegistrationFormDto dto,
                                          Model model,
                                          BindingResult bindingResult,
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

        User registeredUser = userService.registerUser(dto);
        String tokenConfirmationUrl = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

        try{
            eventPublisher.publishEvent(new RegistrationCompleteEvent(registeredUser, tokenConfirmationUrl,
                    LocaleContextHolder.getLocale()));

            redirectAttributes.addFlashAttribute("registrationSuccessful", Boolean.TRUE);
        }catch (Exception e){
            model.addAttribute("emailSendException", Boolean.TRUE);
            return REGISTRATION_PAGE;
        }

        return REDIRECT_TO_LOGIN_PAGE;
    }

    /**
     * Enable user's account is the token is valid and non-expired
     * @param token token from user's email
     * @param redirectAttributes
     * @return
     */
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

    @GetMapping("/resendToken")
    public String displayResentTokenPage(Model model){
        model.addAttribute("dto", new ResendTokenDto());
        return RESEND_TOKEN_PAGE;
    }

    /**
     * For already registered user generate and send new token
     * @param dto
     * @param model
     * @param bindingResult
     * @param redirectAttributes
     * @return
     * @throws UnknownHostException
     */
    @PostMapping("/resendToken")
    public String processResentTokenForm(@ModelAttribute(name = "dto") @Valid ResendTokenDto dto,
                                         Model model,
                                         BindingResult bindingResult,
                                         RedirectAttributes redirectAttributes) throws UnknownHostException {

        if(bindingResult.hasErrors()){
            return RESEND_TOKEN_PAGE;
        }

        if(!userService.isEmailAlreadyTaken(dto.getEmail())){
            model.addAttribute("emailNotRegistered", Boolean.TRUE);
            return RESEND_TOKEN_PAGE;
        }

        VerificationToken generatedToken = userService.generateNewVerificationToken(dto.getEmail());
        String tokenConfirmationUrl = ServletUriComponentsBuilder.fromCurrentRequestUri().toUriString();

        tokenConfirmationUrl = tokenConfirmationUrl.replace("/resendToken", REGISTRATION_CONFIRM_ENDPOINT);
        tokenConfirmationUrl += "?token=";
        tokenConfirmationUrl += generatedToken.getToken();

        SimpleMailMessage mailMessage = mailService.prepareRegistrationMailMessage(tokenConfirmationUrl,
                generatedToken.getUser().getEmail(), LocaleContextHolder.getLocale());

        try{
            mailService.sendEmail(mailMessage);
            redirectAttributes.addFlashAttribute("tokenResend", Boolean.TRUE);
        }catch (Exception e){
            model.addAttribute("emailSendException", Boolean.TRUE);
            return RESEND_TOKEN_PAGE;
        }

        return REDIRECT_TO_LOGIN_PAGE;
    }
}
