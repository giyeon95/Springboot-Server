package com.spring.server.db.mapper;

import com.spring.server.db.dto.User;

import java.util.List;

public interface DBMapper {

    public List<User> getAll() throws Exception;

    public List<User> findByUserId(String id) throws Exception;

    public List<User> userLogin(String id, String pw) throws Exception;

    public int signUpUser(User user) throws Exception;

}
