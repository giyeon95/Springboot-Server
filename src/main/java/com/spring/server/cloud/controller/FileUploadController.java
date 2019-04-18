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

    @RequestMapping("/upload")
    public String upload(MultipartHttpServletRequest request) throws Exception {
        return fileUploadService.restore(request);
    }

    @RequestMapping("/fileRename")
    public String fileRename(HttpServletRequest request) throws Exception {
        return fileUploadService.fileRename(request);
    }

    @RequestMapping("/deleteFile")
    public String deleteFile(HttpServletRequest request) throws Exception {
        return fileUploadService.deleteFile(request);
    }

    // 미완성
    @RequestMapping("/downloadList")
    public String downloadList() {
        return "STring!";
    }

}
