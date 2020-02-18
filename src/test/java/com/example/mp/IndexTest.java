package com.example.mp;

import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import com.example.mp.service.UserPlusService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * <p>[文件描述]：索引速度测试</p >
 * <p>Copyright (c) 2020 Troila </p >
 * <p>Project Name： mp </p >
 *
 * @author chenzheng
 * @version 1.0, 2020/2/18 17:07
 * @since XTJCB
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPlusService userPlusService;

    /**
     * @Description: 插入10万测试数据, 插入时间 15379毫秒
     * @Author: chenzheng
     * @Date: 2020/2/18 17:21
     * @Return: void
     */
    @Test
    public void batchAddUser(){
        long start = System.currentTimeMillis();
        LinkedList<User> users = new LinkedList<>();
        for (int i = 0; i < 100000; i++){
            User user = new User();
            user.setName(String.valueOf(i));
            users.add(user);
        }
        userPlusService.saveBatch(users);
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("--------------------time " + time);
    }

    /**
     * @Description: 不带索引时，查询10万条数据的时间, 249 231毫秒
     * @Author: chenzheng
     * @Date: 2020/2/18 17:22
     * @Return: void
     */
    @Test
    public void test1(){
        long start = System.currentTimeMillis();
        userMapper.selectById(1229702658420645931L);
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("--------------------time " + time);
    }

    /**
     * @Description: 带索引时，查询的时间
     * @Author: chenzheng
     * @Date: 2020/2/18 17:22
     * @Return: void
     */
    public void test2(){
        long start = System.currentTimeMillis();
        userMapper.selectById(1229702658420645931L);
        long end = System.currentTimeMillis();
        long time = end - start;
        log.info("--------------------time " + time);
    }
}
