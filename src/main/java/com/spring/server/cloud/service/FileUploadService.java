package com.spring.server.cloud.service;

import com.spring.server.cloud.dto.FileManager;
import com.spring.server.cloud.mapper.CloudDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileUploadService {

    @Autowired
    CloudDBMapper cloudDBMapper;

    private static final String SAVE_PATH = "/Capstone/cloud"; // Server
    //private static final String SAVE_PATH = "/Users/giyeon/testCloud"; // localhost

    //public String restore(HttpServletRequest request) {
    public String restore(MultipartHttpServletRequest request) {


        String roomId = request.getParameter("roomId");
        String userId = request.getParameter("userId");
        String lastWriteTime = request.getParameter("fileLastWriteTime");
        String saveFileName = request.getParameter("fileName");
        Long length = Long.valueOf(request.getParameter("fileLength"));
        String extension = request.getParameter("fileExtension");




        /*
        String roomId = "12";
        String userId = "ko";
        String lastWriteTime = "2019-04-19 09:08:56";
        String saveFileName = "7s";
        Long length = 32396L;
        String extension = ".Show";
        */

        MultipartFile multipartFile = request.getFile("file");

        FileManager fileManager = new FileManager(roomId,userId,lastWriteTime,saveFileName,length,extension);

        return checkTimeDbUpdate(fileManager, multipartFile);
    }

    private String saveFileName(String extName) {
        Calendar calendar = Calendar.getInstance();
        return UUID.randomUUID().toString();
    }

   private String checkTimeDbUpdate(FileManager fm, MultipartFile multipartFile) {
       String result = "";

       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date originalDate = null;
       Date newDate = null;

        try {
            String dbDate = cloudDBMapper.checkFileTime(fm.getRoomId(),fm.getUserId(),fm.getSaveFileName()); //저장되있는 날짜

            newDate = sdf.parse(fm.getLastWriteTime());


            if(dbDate == null) {
                cloudDBMapper.restoreDatabase(fm);
                writeFile(multipartFile, fm.getSaveFileName());
                result = "addFileSuccess";
            } else {

                originalDate = sdf.parse(dbDate);
                int compare = originalDate.compareTo(newDate);

                if(compare <0) { /** 보낸파일이 더 최신일때 DB UPDATE **/
                    result = "updateFileSuccess";
                    writeFile(multipartFile, fm.getSaveFileName());
                    cloudDBMapper.updateFileTime(fm);
                    //구현완료
                } else if(compare > 0) { /** 보낸파일이 더 옛날파일일때 사용자한테 파일 다시 쏴주어야함 **/

                    // newDate < originalDate // 사용자 파일을 보내줌
                    result = "uploadFile";
                } else { /** 보낸파일이랑 DB값이랑 일치할때**/
                    result = "noChange";
                }
            }

        } catch (Exception e) {
            System.out.println("Error = "+e);
        }

    return result;
   }

    private void writeFile(MultipartFile multipartFile, String saveFileName) throws IOException {
        //System.out.println("file = "+multipartFile);
        byte[] data = multipartFile.getBytes();

        FileOutputStream fos = new FileOutputStream(SAVE_PATH+"/"+saveFileName);
        fos.write(data);
        fos.close();
    }

}
