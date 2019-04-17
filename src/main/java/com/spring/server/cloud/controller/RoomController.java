package com.spring.server.cloud.controller;

import com.spring.server.cloud.dto.UserIdName;
import com.spring.server.cloud.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RoomController {

    @Autowired
    RoomService roomService;

    @RequestMapping("/roomListCheck")
    public @ResponseBody boolean roomListCheck(HttpServletRequest request) throws Exception {
        return roomService.roomListCheck(request);
    }

    @RequestMapping("/userList")
    public @ResponseBody List<UserIdName> userList(HttpServletRequest request)throws Exception {
        return roomService.userList(request);
    }

    @RequestMapping("/searchUserName")
    public @ResponseBody List<UserIdName> searchUserName(HttpServletRequest request) throws Exception {
        return roomService.searchUserName(request);
    }

    @RequestMapping("/createRoom")
    public @ResponseBody int createRoom(HttpServletRequest request) throws Exception {
        return roomService.createRoom(request);
    }


}
