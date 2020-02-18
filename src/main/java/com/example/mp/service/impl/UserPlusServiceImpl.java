package com.example.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import com.example.mp.service.UserPlusService;
import org.springframework.stereotype.Service;

/**
 * <p>[文件描述]：</p >
 * @author chenzheng 2020/2/18 17:14
 */
@Service
public class UserPlusServiceImpl extends ServiceImpl<UserMapper, User> implements UserPlusService {
}
