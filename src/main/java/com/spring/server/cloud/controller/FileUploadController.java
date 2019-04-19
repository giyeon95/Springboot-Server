package com.spring.server.cloud.controller;

import com.spring.server.cloud.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;


@Controller
public class FileUploadController {

    @Autowired
    FileUploadService fileUploadService;

    @RequestMapping("/upload")
    public @ResponseBody String upload(MultipartHttpServletRequest request) throws Exception {
        return fileUploadService.restore(request);
    }

    @RequestMapping("/fileRename")
    public @ResponseBody String fileRename(HttpServletRequest request) throws Exception {
        return fileUploadService.fileRename(request);
    }

    @RequestMapping("/deleteFile")
    public @ResponseBody String deleteFile(HttpServletRequest request) throws Exception {
        return fileUploadService.deleteFile(request);
    }

   @RequestMapping("/firstLoginCheckFileList")
    public @ResponseBody String firstLoginCheckFileList(HttpServletRequest request) throws Exception {
        return fileUploadService.firstLoginCheckFileList(request);
    }

    // 미완성
    @RequestMapping("/downloadList")
    public @ResponseBody String downloadList(HttpServletRequest request) {
    //public @ResponseBody ResponseEntity<Resource> downloadList(HttpServletRequest request) {

        //fileUploadService.downloadList(request);
        return fileUploadService.downloadList(request);
        //return fileUploadService.downloadList();
        //return fileUploadService.downloadList(request);
    }

}
