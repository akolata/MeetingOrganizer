package com.meetingorganizer.controller;

import com.meetingorganizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Controller for displaying images from database
 *
 * @author Aleksander
 */
@Controller
public class ImageController {

    private UserService userService;

    @Autowired
    public ImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/profile/{id}/image")
    public void getUsersProfileImage(@PathVariable long id, HttpServletResponse response) throws IOException {
        byte[] image = userService.findOne(id).getProfilePicture();

        if(image != null){
            response.getOutputStream().write(userService.findOne(id).getProfilePicture());
            response.setContentType("image/jpg");
        }
    }
}
