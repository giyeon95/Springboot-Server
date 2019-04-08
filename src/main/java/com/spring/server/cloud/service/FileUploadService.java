package com.spring.server.cloud.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

@Service
public class FileUploadService {

    private static final String SAVE_PATH = "/Capstone/cloud"; // Server
    //private static final String SAVE_PATH = "/Users/giyeon/testCloud"; // localhost

    public String restore(MultipartHttpServletRequest request) {
        String url = null;
        String id = request.getParameter("id");
        MultipartFile multipartFile = request.getFile("file1");

        try {
            String originFilename = multipartFile.getOriginalFilename();
            Long size = multipartFile.getSize();
            System.out.println(size+"입니다,.");


            String saveFilename = id+"_"+saveFileName(originFilename);

            writeFile(multipartFile, saveFilename);
            url = SAVE_PATH+"/"+saveFilename;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return url;
    }

    private String saveFileName(String extName) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONDAY)+calendar.get(Calendar.DATE)+calendar.get(Calendar.HOUR)+
                "_"+"_"+UUID.randomUUID().toString();
    }

    public String uploadResult() {
        return "success";
    }

    private void writeFile(MultipartFile multipartFile, String saveFileName) throws IOException {
        //System.out.println("file = "+multipartFile);
        byte[] data = multipartFile.getBytes();

        FileOutputStream fos = new FileOutputStream(SAVE_PATH+"/"+saveFileName);
        fos.write(data);
        fos.close();

    }

}
