package com.spring.server.cloud.mapper;

import com.spring.server.cloud.dto.UserIdName;

import java.util.List;

public interface RoomDBMapper {
    public int createRoom(String roomId, String roomPw) throws Exception;

    public List<UserIdName> getAll(String userId) throws Exception;

    public List<UserIdName> searchUserName(String userId, String word) throws Exception;

    public String roomListCheck(String roomId) throws Exception;
}
