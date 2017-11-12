package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.dto.profile.ProfileInfoDto;
import com.meetingorganizer.dto.profile.ProfileMailDto;
import com.meetingorganizer.dto.profile.ProfilePasswordDto;
import com.meetingorganizer.service.UserService;
import com.meetingorganizer.utils.ValidationErrorMessagesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Profile's page controller
 *
 * @author Aleksander
 */
@Controller
@RequestMapping("/profile")
public class ProfileController {

    public static final String PROFILE_PAGE = "profile/profilePage";
    public static final String EDIT_PROFILE_PAGE = "profile/editProfilePage";
    public static final String REDIRECT_TO_PROFILE = "redirect:/profile";

    public static final String USER_ATTRIBUTE = "user";
    public static final String PROFILE_INFO_DTO = "infoDto";
    public static final String PROFILE_MAIL_DTO = "mailDto";
    public static final String PROFILE_PASSWORD_DTO = "passwordDto";

    private UserService userService;
    private ValidationErrorMessagesUtils errorsUtils;

    @Autowired
    public ProfileController(UserService userService, ValidationErrorMessagesUtils errorsUtils) {
        this.userService = userService;
        this.errorsUtils = errorsUtils;
    }

    @GetMapping
    public String displayProfilePage() {
        return PROFILE_PAGE;
    }

    @PostMapping
    public String uploadProfileImage(Authentication authentication,
                                     MultipartFile file,
                                     RedirectAttributes redirectAttributes) {
        User currentUSer = (User) authentication.getPrincipal();


        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileEmptyError");
            return REDIRECT_TO_PROFILE;
        } else if (!file.getContentType().startsWith("image")) {
            redirectAttributes.addFlashAttribute("fileNotImage");
            return REDIRECT_TO_PROFILE;
        }

        try {
            currentUSer.setProfilePicture(file.getBytes());
            userService.saveUserAndFlush(currentUSer);
        } catch (Exception e) {
            return "redirect:/uploadError";
        }

        return PROFILE_PAGE;
    }

    @GetMapping(path = "/edit")
    public String displayEditProfilePage() {
        return EDIT_PROFILE_PAGE;
    }

    @PostMapping(path = "/edit", params = "editInfo")
    public String processEditInfoForm(@Valid @ModelAttribute(name = PROFILE_INFO_DTO) ProfileInfoDto dto,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      Model model) {

        User currentUser = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            return EDIT_PROFILE_PAGE;
        }

        userService.updateUserProfile(currentUser, dto);

        model.addAttribute("updateSuccessful", Boolean.TRUE);
        return EDIT_PROFILE_PAGE;
    }

    @PostMapping(path = "/edit", params = "editMail")
    public String processEditMailForm(@Valid @ModelAttribute(name = PROFILE_MAIL_DTO) ProfileMailDto dto,
                                      BindingResult bindingResult,
                                      Authentication authentication,
                                      Model model) {
        User currentUser = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                model.addAllAttributes(
                        errorsUtils.errorMessagesForClassLevelValidations(bindingResult.getGlobalErrors())
                );
            }

            return EDIT_PROFILE_PAGE;
        }

        if (!isNewEmailAvailable(dto.getEmail(), currentUser)) {
            model.addAttribute("emailAlreadyTaken", Boolean.TRUE);
            return EDIT_PROFILE_PAGE;
        }

        userService.updateUserProfile(currentUser, dto);

        model.addAttribute("updateSuccessful", Boolean.TRUE);
        return EDIT_PROFILE_PAGE;
    }

    @PostMapping(path = "/edit", params = "editPassword")
    public String processEditPasswordForm(@Valid @ModelAttribute(name = PROFILE_PASSWORD_DTO) ProfilePasswordDto dto,
                                          BindingResult bindingResult,
                                          Authentication authentication,
                                          Model model) {

        User currentUser = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                model.addAllAttributes(
                        errorsUtils.errorMessagesForClassLevelValidations(bindingResult.getGlobalErrors())
                );
            }

            return EDIT_PROFILE_PAGE;
        }

        if (!userService.passwordMatchesStoredPassword(dto.getOldPassword(), currentUser)) {
            model.addAttribute("currentPasswordNotEqual", Boolean.TRUE);
            return EDIT_PROFILE_PAGE;
        }

        userService.updateUserProfile(currentUser, dto);

        model.addAttribute("updateSuccessful", Boolean.TRUE);
        return EDIT_PROFILE_PAGE;
    }

    private boolean isNewEmailAvailable(String email, User currentUser) {
        boolean isEmailAvailable = false;
        boolean isEmailAlreadyTaken = userService.isEmailAlreadyTaken(email);

        if (isEmailAlreadyTaken) {
            boolean isEmailSameAsBefore = currentUser.getEmail().equalsIgnoreCase(email);

            if (isEmailSameAsBefore) {
                isEmailAvailable = true;
            }

        } else {
            isEmailAvailable = true;
        }

        return isEmailAvailable;
    }

    @ModelAttribute
    public void profilePageDto(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        model.addAttribute(USER_ATTRIBUTE, currentUser);
        model.addAttribute(PROFILE_INFO_DTO, new ProfileInfoDto(currentUser));
        model.addAttribute(PROFILE_MAIL_DTO, new ProfileMailDto());
        model.addAttribute(PROFILE_PASSWORD_DTO, new ProfilePasswordDto());
    }

}
