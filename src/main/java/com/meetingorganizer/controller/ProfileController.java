package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.dto.EditProfileDto;
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
import java.io.IOException;

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

    private UserService userService;
    private ValidationErrorMessagesUtils errorsUtils;

    @Autowired
    public ProfileController(UserService userService, ValidationErrorMessagesUtils errorsUtils) {
        this.userService = userService;
        this.errorsUtils = errorsUtils;
    }

    @GetMapping
    public String displayProfilePage(Authentication authentication, Model model) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("dto", currentUser);

        return PROFILE_PAGE;
    }

    @PostMapping
    public String uploadProfileImage(Authentication authentication,
                                     Model model,
                                     MultipartFile file,
                                     RedirectAttributes redirectAttributes) throws IOException {
        User currentUSer = (User) authentication.getPrincipal();
        model.addAttribute("dto", currentUSer);

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("fileEmptyError");
            return REDIRECT_TO_PROFILE;
        } else if (!file.getContentType().startsWith("image")) {
            redirectAttributes.addFlashAttribute("fileNotImage");
            return REDIRECT_TO_PROFILE;
        }

        currentUSer.setProfilePicture(file.getBytes());
        userService.saveUserAndFlush(currentUSer);

        return PROFILE_PAGE;
    }

    @GetMapping(path = "/edit")
    public String displayEditProfilePage(Authentication authentication, Model model) {
        User currentUser = (User) authentication.getPrincipal();
        model.addAttribute("dto", new EditProfileDto(currentUser));

        return EDIT_PROFILE_PAGE;
    }

    @PostMapping(path = "/edit")
    public String processEditProfileForm(@Valid @ModelAttribute(name = "dto") EditProfileDto dto,
                                         BindingResult bindingResult,
                                         Authentication authentication,
                                         Model model) {

        User currentUser = (User) authentication.getPrincipal();

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasGlobalErrors()) {
                model.addAllAttributes(
                        errorsUtils.errorMessagesForClassLevelValidations(bindingResult.getGlobalErrors()));
            }

            return EDIT_PROFILE_PAGE;
        }

        if(userService.isEmailAlreadyTaken(dto.getEmail()) && !(currentUser.getEmail().equalsIgnoreCase(dto.getEmail()))){
            model.addAttribute("emailAlreadyTaken", Boolean.TRUE);
            return EDIT_PROFILE_PAGE;
        }

        if(!userService.isPasswordDifferentThanCurrent(dto.getPassword(), currentUser)){
            model.addAttribute("currentPasswordNotEqual", Boolean.TRUE);
            return EDIT_PROFILE_PAGE;
        }

        userService.updateUserProfile(currentUser, dto);
        return EDIT_PROFILE_PAGE;
    }

}
