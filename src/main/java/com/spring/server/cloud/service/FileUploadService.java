package com.spring.server.cloud.service;

import com.spring.server.Common;
import com.spring.server.cloud.dto.FileManager;
import com.spring.server.cloud.dto.FileNameTime;
import com.spring.server.cloud.dto.FileRename;
import com.spring.server.cloud.mapper.CloudDBMapper;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.xml.transform.ResourceSource;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileUploadService {

    private static final Log logger = LogFactory.getLog(FileUploadService.class );
    @Autowired
    CloudDBMapper cloudDBMapper;


    private static final String SAVE_PATH = "/Capstone/cloud/";// Server
    private static final String SAVE_ZIP_PATH = "/Capstone/.cloud/";
    //private static final String SAVE_PATH = "/Users/giyeon/testCloud"; // localhost

    //public String restore(HttpServletRequest request) {
    public String restore(MultipartHttpServletRequest request) throws Exception{


        String roomId = request.getParameter("roomId");
        String userId = request.getParameter("userId");
        String lastWriteTime = request.getParameter("fileLastWriteTime");
        String saveFileName = request.getParameter("fileName");
        Long length = Long.valueOf(request.getParameter("fileLength"));
        String extension = request.getParameter("fileExtension");

        createFolder(roomId);

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

    public String fileRename(HttpServletRequest request) throws Exception{
        String newFileName = request.getParameter("newFileName");
        String newExtension = request.getParameter("newExtension");
        String oldFileName = request.getParameter("oldFileName");
        String oldExtension = request.getParameter("oldExtension");

        String roomId = request.getParameter("roomId");
        String userId = request.getParameter("userId");

        FileRename fileRenameSet = new FileRename(newFileName, newExtension, oldFileName, oldExtension, roomId, userId);

        File file = new File(SAVE_PATH+roomId+"/"+oldFileName);
        File newFile = new File(SAVE_PATH+roomId+"/"+newFileName);
        if(file.exists()) {
            if (file.renameTo(newFile)) {
                cloudDBMapper.fileRename(fileRenameSet);
            }
        }
        return "success";
    }

    public String deleteFile(HttpServletRequest request) throws Exception {

        String result;
        String roomId = request.getParameter("roomId");
        String saveFileName = request.getParameter("fileName");
        FileManager fileManager = new FileManager(roomId,
                                                    request.getParameter("userId"),
                                                    "2000-01-01 10:00:00",
                                                    saveFileName,
                                                    0L,
                                                    request.getParameter("fileExtension"));

        int num = cloudDBMapper.deleteFile(fileManager);

        if(num == 0) {
            result = "서버에 없는 파일"; //새로 다운로드를 할것인지 요청 그리고 새로다운로드를 안받고 지울것인지 물어봐서 지우면 그때 진짜 삭제
        } else {
            File file = new File(SAVE_PATH+roomId+"/"+saveFileName);
            file.delete();
            result = "success";
        }

        return result;
    }

    public String firstLoginCheckFileList(HttpServletRequest request) throws Exception {

        StringBuffer json = Common.getInstance().createJson(request);
        List<String> notExistFileList = new ArrayList<String>();
        List<String> updateNeedFileList = new ArrayList<String>();
        List<String> latestFile = new ArrayList<String>();
        List<String> allGetFile = new ArrayList<String>();

        String result ="";
        String roomId;
        String userId;
        int dbCount;
        int localCount;

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());

            roomId = jsonObject.get("roomId").toString();
            userId = jsonObject.get("userId").toString();

            JSONObject jsonObjectFiles = (JSONObject)jsonObject.get("files");

            JSONObject fileObject;
            FileManager fileManager;
            String fileName;
            String localDate;

            dbCount = cloudDBMapper.checkDBCount(roomId);
            localCount = Integer.parseInt(jsonObject.get("length").toString());

            for(int i=0 ; i < localCount; i++) {
                 fileObject = (JSONObject)jsonObjectFiles.get(String.valueOf(i));


                 fileName = fileObject.get("fileName").toString();
                 localDate = fileObject.get("fileLastWriteTime").toString();


                fileManager = new FileManager(roomId, userId,
                                            localDate,
                                            fileName,
                                            Long.valueOf(fileObject.get("fileLength").toString()),
                                            fileObject.get("fileExtension").toString());

                allGetFile.add(fileName);

                String dbFileName = cloudDBMapper.checkExistFileList(fileManager);

                if(dbFileName == null) {
                    notExistFileList.add(fileName);
                } else {
                    String dbTime = cloudDBMapper.checkFileTime(roomId,fileName);
                    int tmp = checkDate(localDate, dbTime);

                    if(tmp <0) { /** 보낸파일이 더 최신일때 DB UPDATE **/
                        notExistFileList.add(fileName);

                    } else if(tmp > 0) { /** 보낸파일이 더 옛날파일일때 사용자한테 파일 다시 쏴주어야함 **/
                        updateNeedFileList.add(fileName);
                        //사용자한테 재전송해야함 사용자파일이 옛날꺼임

                    } else { /** 보낸파일이랑 DB값이랑 일치할때**/
                        latestFile.add(fileName);
                    }
                }
            }

            if(dbCount > localCount) {
                if(allGetFile.size()>0) {

                    HashMap<String, Object> mapParam = new HashMap<String, Object>();
                    mapParam.put("roomId",roomId);
                    mapParam.put("list",allGetFile);

                    List<Map<String, Object>> list = cloudDBMapper.getUserDownloadList(mapParam);

                    if(!list.isEmpty()) {
                        list.forEach((item) -> {
                            updateNeedFileList.add(item.get("FileName").toString());
                        });
                    }
                } else { // case 0
                    List<Map<String, Object>> list = cloudDBMapper.nullGetUserFolder(roomId);
                    if(!list.isEmpty()) {
                        list.forEach((item) -> {
                            updateNeedFileList.add(item.get("FileName").toString());
                        });
                    }
                }

            }


            File file = new File(SAVE_ZIP_PATH);


            JSONObject sendObject = new JSONObject();
            sendObject.put("uploadList",notExistFileList);
            sendObject.put("downloadList",updateNeedFileList);
            sendObject.put("latestFile",latestFile);

            sendObject.put("uploadSize",notExistFileList.size());
            sendObject.put("downloadSize",updateNeedFileList.size());


            result = sendObject.toJSONString();
            logger.info(sendObject.toJSONString());

        }catch(Exception e) {
            logger.error(e);
            e.printStackTrace();
        }


        return result;
    }

    public Resource downloadList(HttpServletRequest request) {


        String zipFileName = "";
        StringBuffer json = Common.getInstance().createJson(request);
        JSONParser parser = new JSONParser();
        List<File> files = new ArrayList<File>();

        File file;
        String filePath;


        try {
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());
            String roomId = jsonObject.get("roomId").toString();
            String userId = jsonObject.get("userId").toString();

            JSONArray jsonArray = (JSONArray)jsonObject.get("uploadList");


           for(int i = 0 ; i < jsonArray.size() ; i++) {
               filePath = SAVE_PATH+roomId+"/"+jsonArray.get(i).toString();

               file = new File(filePath);
               if(file.exists()) files.add(file);
           }

           zipFileName = SAVE_ZIP_PATH+roomId+userId+".zip";

           File zippedFile = new File(zipFileName);
            ZipArchiveOutputStream out = new ZipArchiveOutputStream(
                    new FileOutputStream(zippedFile));

            makeZip(files, out);

        } catch (ParseException e) {
            logger.error("ParseException : "+e);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFound : "+e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Exception : "+e);
            e.printStackTrace();
        }

        //return callDownloadFile(SAVE_PATH+"cloud/aa1/이종혁.txt");
        return callDownloadFile(zipFileName);
    }

    public String resultResponeReturnDateTime(HttpServletRequest request) throws Exception {
        StringBuffer json = Common.getInstance().createJson(request);
        JSONParser parser = new JSONParser();
        List<String> resultList = new ArrayList<String>();
        String returnJSON = "";

        try {
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());
            String roomId = jsonObject.get("roomId").toString();
            String userId = jsonObject.get("userId").toString();
            String result = jsonObject.get("result").toString();

            if(result.equals("true")) {
                File file = new File(SAVE_ZIP_PATH+roomId+userId+".zip");
                if(file.exists()) file.delete();
            }

            JSONArray jsonArray = (JSONArray)jsonObject.get("uploadList");
            for(int i = 0 ; i < jsonArray.size() ; i++) {
                resultList.add(jsonArray.get(i).toString());
            }

            HashMap<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("roomId",roomId);
            mapParam.put("list",resultList);

            List<Map<String, Object>> list = cloudDBMapper.returnDBDateTime(mapParam);


            JSONObject jsonObj = new JSONObject();

            if(!list.isEmpty()) {
                int i;
                JSONObject itemObj;
                for(i = 0 ; i< list.size() ; i++) {

                    itemObj = new JSONObject();

                    itemObj.put("FileName",list.get(i).get("FileName").toString());

                    itemObj.put("Time", cloudDBMapper.checkFileTime(roomId, list.get(i).get("FileName").toString()));

                    jsonObj.put(String.valueOf(i),itemObj);

                }
                jsonObj.put("length", i);
            }
            returnJSON = jsonObj.toJSONString();

        } catch (Exception e) {
            logger.error(e);
        }
        return returnJSON;
    }

    /**DEBUG TEST JSON**/
    public void debugTest(HttpServletRequest request) throws Exception {
        String roomId = request.getParameter("roomId");
        //String parm1 = request.getParameter("file1");
        //String parm2 = request.getParameter("file2");
        //String parm3 = request.getParameter("file3");


        List<String> arr = new ArrayList<String>();

        //arr.add(parm1);
        //arr.add(parm2);
        //arr.add(parm3);

        HashMap<String, Object> mapParam = new HashMap<String, Object>();
        mapParam.put("roomId",roomId);
        mapParam.put("list",arr);


        int dbCount = cloudDBMapper.checkDBCount(roomId);

        if(arr.size() > 0) {
            List<Map<String, Object>> list = cloudDBMapper.getUserDownloadList(mapParam);
            if(!list.isEmpty()) {
                list.forEach((item) -> {
                    System.out.println(item.get("FileName"));
                });
            }
        } else { // case 0
            List<Map<String, Object>> list = cloudDBMapper.nullGetUserFolder(roomId);
            if (!list.isEmpty()) {
                list.forEach((item) -> {
                    System.out.println(item.get("FileName"));
                    //updateNeedFileList.add(item.toString());
                });
            }

        }

    }

    private Resource callDownloadFile(String filePath) {

        try {
            Path path = Paths.get(filePath).toAbsolutePath().normalize();
            Resource resource = new UrlResource(path.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new Exception ("File not found : "+filePath);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    private void makeZip(List<File> files, ZipArchiveOutputStream out) throws Exception {
        byte[] buf = new byte[8 * 1024];
        out.setEncoding("UTF-8");
        FileInputStream fis = null;
        ZipArchiveEntry ze;

        int length;

        if(files.size() > 0) {
            for (int i = 0; i < files.size(); i++) {

                ze = new ZipArchiveEntry(files.get(i).getName());
                out.putArchiveEntry(ze);
                fis = new FileInputStream(files.get(i));
                while ((length = fis.read(buf, 0, buf.length)) >= 0) {
                    out.write(buf, 0, length);
                }
            }
            fis.close();
            out.closeArchiveEntry();
        }
        out.close();
    }

    private void createFolder(String roomId) {
        File file = new File(SAVE_PATH+roomId);
        logger.info("FileUploadService CreateFolder: "+SAVE_PATH+roomId);
        if(!file.exists()) file.mkdir();

    }

    private int checkDate(String localDate, Object dbDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date LocalDate = null;
        Date DBDate = null;

        int compare = 0;
        try {
            DBDate = sdf.parse(dbDate.toString());
            LocalDate = sdf.parse(localDate);

            compare = DBDate.compareTo(LocalDate);

        } catch (Exception e) {

        }
        return compare;
    }

    private String checkTimeDbUpdate(FileManager fm, MultipartFile multipartFile) {
       String result = "";

       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date originalDate = null;
       Date newDate = null;

        try {
            String dbDate = cloudDBMapper.checkFileTime(fm.getRoomId(),fm.getSaveFileName()); //저장되있는 날짜

            newDate = sdf.parse(fm.getLastWriteTime());


            if(dbDate == null) {
                cloudDBMapper.restoreDatabase(fm);
                writeFile(multipartFile, fm.getRoomId(), fm.getSaveFileName());
                result = "addFileSuccess";
            } else {

                originalDate = sdf.parse(dbDate);
                int compare = originalDate.compareTo(newDate);

                if(compare <0) { /** 보낸파일이 더 최신일때 DB UPDATE **/
                    result = "updateFileSuccess";
                    writeFile(multipartFile, fm.getRoomId(),fm.getSaveFileName());
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

    private void writeFile(MultipartFile multipartFile, String roomId, String saveFileName) throws IOException {
        //System.out.println("file = "+multipartFile);
        byte[] data = multipartFile.getBytes();

        FileOutputStream fos = new FileOutputStream(SAVE_PATH+roomId+"/"+saveFileName);
        fos.write(data);
        fos.close();
    }

}
