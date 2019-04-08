package com.spring.server.db.service;

import com.google.gson.JsonIOException;
import com.spring.server.db.dto.User;
import com.spring.server.db.mapper.DBMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.util.List;

@Service
public class DBService {

    @Autowired
    DBMapper dbMapper; //빨간줄 ok

    public List<User> getAll() throws Exception {
        return dbMapper.getAll();
    }
    public List<User> findByUserId(String id) throws Exception {
        return dbMapper.findByUserId(id);
    }

    public boolean userLogin(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        String pw = request.getParameter("pw");

        List<User> list = dbMapper.userLogin(id,pw);

        return !list.isEmpty(); //일치 true
    }

    public int signUpUser(HttpServletRequest request) throws Exception {

        StringBuffer json = new StringBuffer();
        String line = null;

        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }

        }catch(Exception e) {
            System.out.println("Error reading JSON string: " + e.toString());
        }


        String name,id,password;
        User user = new User();


       try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());

            JSONObject jsonObjectData = (JSONObject)jsonObject.get("data");


            name  = jsonObjectData.get("name").toString();
            id = jsonObjectData.get("id").toString();
            password = jsonObjectData.get("password").toString();

            user.pushUser(name,id,password);

        } catch(JsonIOException e) {
            e.printStackTrace();
        }

        return dbMapper.signUpUser(user);
    }



}
