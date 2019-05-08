package com.spring.server.cloud.mapper;

import com.spring.server.cloud.dto.UserIdName;

import java.util.List;
import java.util.Map;

public interface RoomDBMapper {
    public int createRoom(String roomId, String roomPw, String userId) throws Exception;

    public List<UserIdName> getAll(String userId) throws Exception;

    public List<UserIdName> searchUserName(String userId, String word) throws Exception;

    public String roomListCheck(String roomId) throws Exception;

    public int roomEnter(String roomId, String roomPw) throws Exception;

    public int roomEnterOneMoreCheck(String userId, String roomId);

    public List<Map<String, Object>> eachUserRoomList(String userId, String word) throws Exception;

    public int roomEnterOneMoreCheck(String userId, String roomId);

}
