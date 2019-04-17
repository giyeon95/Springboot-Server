package com.spring.server.db.mapper;

import com.spring.server.db.dto.User;
import com.spring.server.db.dto.UserEmail;

import java.util.List;

public interface DBMapper {

    public List<User> getAll() throws Exception;

    public List<User> findByUserId(String id) throws Exception;

    public List<User> userLogin(String id, String pw) throws Exception;

    public int signUpUser(User user) throws Exception;

    public int addUserRoom(String roomId, String userId) throws Exception;

    public List<UserEmail> emailReturn(String roomId) throws Exception;
}
