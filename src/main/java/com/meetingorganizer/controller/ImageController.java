package com.meetingorganizer.controller;

import com.meetingorganizer.domain.User;
import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for displaying images from database
 *
 * @author Aleksander
 */
@Controller
@RequestMapping("/profile/{id}/image")
public class ImageController {

    private UserService userService;

    @Autowired
    public ImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public void getUsersProfileImage(@PathVariable long id, HttpServletResponse response) throws IOException {
        User user = userService.findOne(id);
        byte[] image = null;

        if(user != null){
            image  = user.getProfilePicture();
        }

        if(image != null){
            response.getOutputStream().write(userService.findOne(id).getProfilePicture());
            response.setContentType("image/jpg");
        }
    }
}
