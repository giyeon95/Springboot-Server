package com.spring.server.cloud.controller;

import com.spring.server.cloud.service.FileUploadService;
import org.apache.commons.io.IOExceptionWithCause;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Controller
public class FileUploadController {

    private static final Log logger = LogFactory.getLog(FileUploadController.class );

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

    @RequestMapping("/debugTest")
    public @ResponseBody String debugTest(HttpServletRequest request) throws Exception {
        fileUploadService.debugTest(request);
        return "debugTestPage";
    }

    // 미완성
    @RequestMapping("/downloadList")
    public @ResponseBody ResponseEntity<Resource> downloadList(HttpServletRequest request) throws Exception{

        Resource resource = fileUploadService.downloadList(request);
        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info(ex);
        }

        if(contentType == null) {
            //contentType = "text";
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + resource.getFilename())
                .body(resource);
    }

}
