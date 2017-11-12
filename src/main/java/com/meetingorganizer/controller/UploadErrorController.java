package com.meetingorganizer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleksander
 */
@Controller
public class UploadErrorController {

    public static final String UPLOAD_ERROR_PAGE = "error/uploadErrorPage";

    @RequestMapping("/uploadError")
    public String showUploadErorPage() {
        System.out.println("HERE");
        return UPLOAD_ERROR_PAGE;
    }
}
