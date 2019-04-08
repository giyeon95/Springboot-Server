package com.spring.server.cloud.controller;

import com.spring.server.cloud.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @RequestMapping("/hello")
    public String onView() {
        return "hello";
    }

    @RequestMapping("/upload")
    public String upload(MultipartHttpServletRequest request) {
            fileUploadService.restore(request);
        return fileUploadService.uploadResult();
    }

}
