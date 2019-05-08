package com.spring.server.cloud.mapper;

import com.spring.server.cloud.dto.FileManager;
import com.spring.server.cloud.dto.FileRename;

import java.util.List;
import java.util.Map;


public interface CloudDBMapper {

    public int restoreDatabase(FileManager fileManager) throws Exception;

    public String checkFileTime(String roomId, String fileName) throws Exception;

    public boolean updateFileTime(FileManager fileManager) throws Exception;

    public boolean fileRename(FileRename fileRename) throws Exception;

    public int deleteFile(FileManager fileManager) throws Exception;

    public String checkExistFileList(FileManager fileManager) throws Exception;

    public List<Map<String, Object>> nullGetUserFolder(String roomId) throws Exception;

    public int checkDBCount(String roomId) throws Exception;

    public List<Map<String, Object>> getUserDownloadList(Map<String, Object> map) throws Exception;

    public List<Map<String, Object>> returnDBDateTime(Map<String, Object> map) throws Exception;
}
