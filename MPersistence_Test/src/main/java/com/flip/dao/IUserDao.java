package com.flip.dao;

import com.flip.pojo.User;

import java.util.List;

public interface IUserDao {
    //query all user
    public List<User> findAll();

    //query by condition
    public User findByCondition(User user);


}
