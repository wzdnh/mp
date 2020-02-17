package com.example.mp.service.impl;

import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import com.example.mp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>[文件描述]：业务接口实现类</p >
 *
 * @author chenzheng
 * @data 2020/2/17 16:43
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> get() {
        return userMapper.selectList(null);
    }

    @Override
    public void addUser(User user) {
        if (userMapper.insert(user) != 1){
            log.error("------------添加错误");
        }else{
            log.error("------------添加成功");
        };
    }

    @Override
    public void setUser(User user) {
        if (userMapper.updateById(user) != 1){
            log.error("----------------------修改错误");
        }else{
            log.error("------------修改成功");
        };
    }
}
