package com.example.mp.service;

import com.example.mp.entity.User;

import java.util.List;

/**
 * <p>[文件描述]：业务接口</p >
 *
 * @author chenzheng
 * @date 2020/2/17 16:42
 */

public interface UserService {

    List<User> get();

    void addUser(User user);

    void setUser(User user);


}
