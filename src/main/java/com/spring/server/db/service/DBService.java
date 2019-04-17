package com.spring.server.db.service;

import com.google.gson.JsonIOException;
import com.spring.server.db.dto.User;
import com.spring.server.db.dto.UserEmail;
import com.spring.server.db.mapper.DBMapper;
import org.json.simple.JSONArray;
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

        User user = new User();

        StringBuffer json = createJson(request);
        try {

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());

            JSONObject jsonObjectData = (JSONObject)jsonObject.get("data");

            user.pushUser(jsonObjectData.get("name").toString(),
                    jsonObjectData.get("id").toString(),
                    jsonObjectData.get("password").toString(),
                    jsonObjectData.get("email").toString());

        }catch(Exception e) {
            e.printStackTrace();
        }

        return dbMapper.signUpUser(user);
    }

    public List<UserEmail> approvalUserRoom (HttpServletRequest request) throws Exception {

        UserEmail userEmail = new UserEmail();

        StringBuffer json = createJson(request);
        String roomId="" , userId;

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(json.toString());

            roomId = jsonObject.get("roomID").toString();

            JSONArray jsonArray = (JSONArray)jsonObject.get("approvalUser");

            for(int i = 0 ; i < Integer.parseInt(jsonObject.get("count").toString()) ; i++) {
                dbMapper.addUserRoom(roomId, jsonArray.get(i).toString());
            }



        }catch(Exception e) {
            e.printStackTrace();
        }



        return dbMapper.emailReturn(roomId);
    }

    private StringBuffer createJson(HttpServletRequest request) {
        String line = null;
        StringBuffer json = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            while((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


}
