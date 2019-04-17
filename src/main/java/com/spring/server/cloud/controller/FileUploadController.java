package com.spring.server.cloud.controller;

import com.spring.server.cloud.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


@Controller
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @RequestMapping("/hello")
    public String onView() {
        return "hello";
    }

    @RequestMapping("/upload")

            //public String upload(HttpServletRequest request) {
    public String upload(MultipartHttpServletRequest request) {
        return fileUploadService.restore(request);
    }

    // 미완성
    @RequestMapping("/downloadList")
    public String downloadList() {
        return "STring!";
    }

}
