package com.spring.server.cloud.dto;

public class FileManager {
    private String roomId;
    private String userId;
    private String lastWriteTime;
    private String saveFileName;
    private Long fileLength;
    private String extension;

    public FileManager(String roomId, String userId, String lastWriteTime, String saveFileName, Long fileLength, String extension) {
        this.roomId = roomId;
        this.userId = userId;
        this.lastWriteTime = lastWriteTime;
        this.saveFileName = saveFileName;
        this.fileLength = fileLength;
        this.extension = extension;

    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastWriteTime() {
        return lastWriteTime;
    }

    public void setLastWriteTime(String lastWriteTime) {
        this.lastWriteTime = lastWriteTime;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public void setSaveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
    }



    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getFileLength() {
        return fileLength;
    }

    public void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }
}
