package com.spring.server.cloud.mapper;

import com.spring.server.cloud.dto.FileManager;


public interface CloudDBMapper {

    public int restoreDatabase(FileManager fileManager) throws Exception;

    public String checkFileTime(String roomId, String userId, String fileName) throws Exception;

    public boolean updateFileTime(FileManager fileManager) throws Exception;
}
