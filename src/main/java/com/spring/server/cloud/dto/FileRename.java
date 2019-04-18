package com.spring.server.cloud.dto;

public class FileRename {

    private String newFileName;
    private String newExtension;
    private String oldFileName;
    private String oldExtension;
    private String roomId;
    private String userId;

    public FileRename(String newFileName, String newExtension, String oldFileName, String oldExtension, String roomId, String userId) {
        this.newFileName = newFileName;
        this.newExtension = newExtension;
        this.oldFileName = oldFileName;
        this.oldExtension = oldExtension;
        this.roomId = roomId;
        this.userId = userId;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getNewExtension() {
        return newExtension;
    }

    public void setNewExtension(String newExtension) {
        this.newExtension = newExtension;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getOldExtension() {
        return oldExtension;
    }

    public void setOldExtension(String oldExtension) {
        this.oldExtension = oldExtension;
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
}
