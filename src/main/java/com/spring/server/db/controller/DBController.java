package com.spring.server.db.controller;

import com.spring.server.db.dto.User;
import com.spring.server.db.dto.UserEmail;
import com.spring.server.db.service.DBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class DBController {

    @Autowired
    DBService dbService;

    @RequestMapping("/query")
    public @ResponseBody List<User> query() throws Exception {
        return dbService.getAll();
    }

    @RequestMapping("/findId")
    public @ResponseBody List<User> findId(HttpServletRequest request) throws Exception {
        String param = request.getParameter("id");
        return dbService.findByUserId(param);
    }

    @RequestMapping("/userLogin")
    public @ResponseBody boolean login(HttpServletRequest request) throws Exception {
        return dbService.userLogin(request);
    }

    //JSON OBJECT POST
    @RequestMapping("/addUser")
    public @ResponseBody int addUser(HttpServletRequest request) throws Exception {
       return dbService.signUpUser(request);
    }

    @RequestMapping("/approvalUserRoom")
    public @ResponseBody List<UserEmail> approvalUserRoom(HttpServletRequest request) throws Exception {
        return dbService.approvalUserRoom(request);
    }

}
