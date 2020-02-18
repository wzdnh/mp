package com.example.mp.service.impl;

import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import com.example.mp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>[文件描述]：带事务的实现类</p >
 * <p>Copyright (c) 2020 Troila </p >
 * <p>Project Name： mp </p >
 *
 * @author chenzheng
 * @version 1.0, 2020/2/18 13:16
 * @since XTJCB
 */
@Slf4j
@Service("UserServiceTransaction")
@Transactional(value = "transactionManager", isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,rollbackFor = Exception.class,timeout=36000)
public class UserServiceImplTransaction implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public List<User> get() {
        log.info("--------------事务-查询");
        return userMapper.selectList(null);
    }

    @Override
    public void addUser(User user) {
        if (userMapper.insert(user) != 1){
            log.error("------------事务-添加错误");
        }else{
            log.error("------------事务-添加成功");
        };
    }

    @Override
    public void setUser(User user) {
        if (userMapper.updateById(user) != 1){
            log.error("----------------------事务-修改错误");
        }else{
            log.error("------------事务-修改成功");
        };
    }
}
