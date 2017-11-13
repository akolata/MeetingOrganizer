package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksander
 */
@Controller
@RequestMapping("/uploadError")
public class UploadErrorController {

    public static final String UPLOAD_ERROR_PAGE = "error/uploadErrorPage";

    @GetMapping
    public String showUploadErorPage() {
        return UPLOAD_ERROR_PAGE;
    }
}
