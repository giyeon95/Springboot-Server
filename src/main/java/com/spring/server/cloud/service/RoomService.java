package com.spring.server.cloud.service;

import com.spring.server.cloud.dto.UserIdName;
import com.spring.server.cloud.mapper.RoomDBMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {

    @Autowired
    RoomDBMapper roomDBMapper;

    //createRoom_Check room_id

    public boolean roomListCheck(HttpServletRequest request) throws Exception {
        boolean isRoom = true;
        String roomId = request.getParameter("roomID");
        String roomName = roomDBMapper.roomListCheck(roomId);
        if (roomName == null) {
            isRoom = false;
        }
        return isRoom;
    }

    public List<UserIdName> userList(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userID");
        return roomDBMapper.getAll(userId);
    }

    public List<UserIdName> searchUserName(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userID");
        String word = request.getParameter("word");
        word = "%" + word + "%";

        return roomDBMapper.searchUserName(userId, word);
    }

    public int createRoom(HttpServletRequest request) throws Exception {
        String roomId = request.getParameter("roomID"); //수정
        String roomPw = request.getParameter("PW"); //수정
        String userId = request.getParameter("userID");

        return roomDBMapper.createRoom(roomId, roomPw,userId);
    }

    public int roomEnter(HttpServletRequest request) throws Exception {
        int result;
        String userId = request.getParameter("userID");
        String roomId = request.getParameter("roomID");
        String roomPw = request.getParameter("PW");

        if(roomDBMapper.roomEnter(roomId, roomPw) == 1) {
            if(roomDBMapper.roomEnterOneMoreCheck(userId, roomId) ==0) {
                result = -1;
            } else {
                result =1;
            }
        } else {
            result = 0;
        }

        return result;
    }

    public List<Map<String, Object>> eachUserRoomList(HttpServletRequest request) throws Exception {
        String userId = request.getParameter("userID");
        String word = request.getParameter("word");
        word = "%"+word+"%";
        return roomDBMapper.eachUserRoomList(userId, word);
    }

}
