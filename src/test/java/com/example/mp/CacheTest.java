package com.example.mp;

import com.example.mp.dao.UserMapper;
import com.example.mp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>[文件描述]：缓存测试</p >
 * <p>Copyright (c) 2020 Troila </p >
 * <p>Project Name： mp </p >
 *
 * @author chenzheng
 * @version 1.0, 2020/2/18 13:30
 * @since XTJCB
 */
@Slf4j
@RunWith(SpringRunner.class)    //运行器
@SpringBootTest
public class CacheTest {

    @Autowired
    private UserMapper userMapper;
    @Qualifier("UserServiceTransaction")
    @Autowired
    private UserService userTransactionalService;
    @Qualifier("userService")
    @Autowired
    private UserService userService;

    /**
     * @Description: 单线程环境测试一级缓存，不带事务
     * Creating a new SqlSession 3次
     * JDBC Connection 同一个
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test1() {
        for (int i = 0; i < 3; i++) {
            userService.get();
        }
    }

    /**
     * @Description: 单线程环境测试一级缓存，service实现类添加事务
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test2() {
        for (int i = 0; i < 3; i++) {
            userTransactionalService.get();
        }

    }

    /**
     * @Description: 单线程环境测试二级缓存，service实现类添加事务, userMapper添加@cacheNameSpace注解
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test3() {
        for (int i = 0; i < 3; i++) {
            userTransactionalService.get();
        }
    }

    /**
     * @Description: 多线程环境测试一级缓存，不带事务
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test() throws InterruptedException {
        System.out.println("使用线程池运行 Runnable 任务：");

        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 创建大小固定为 5 的线程池

        List<Runnable> tasks = new ArrayList<>(10);

        for (int i = 0; i < 3; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    userService.get();
                }
            };

            tasks.add(task);

            threadPool.execute(task); // 让线程池执行任务 task
        }
        threadPool.shutdown(); // 向线程池发送关闭的指令，等到已经提交的任务都执行完毕之后，线程池会关闭
        threadPool.awaitTermination(1, TimeUnit.HOURS); // 等待线程池关闭，等待的最大时间为 1 小时
    }

    /**
     * @Description: 多线程环境测试一级缓存, service实现类添加事务
     * @Author: chenzheng
     * @Date: 2020/2/17 16:35
     * @param: null
     * @Return:
     */
    @Test
    public void testCache() throws InterruptedException {
        System.out.println("使用线程池运行 Runnable 任务：");

        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 创建大小固定为 5 的线程池

        List<Runnable> tasks = new ArrayList<>(10);

        for (int i = 0; i < 3; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    userTransactionalService.get();
                }
            };

            tasks.add(task);

            threadPool.execute(task); // 让线程池执行任务 task
        }
        threadPool.shutdown(); // 向线程池发送关闭的指令，等到已经提交的任务都执行完毕之后，线程池会关闭

        threadPool.awaitTermination(1, TimeUnit.HOURS); // 等待线程池关闭，等待的最大时间为 1 小时

    }


    /**
     * @Description: 多线程环境测试二级缓存, service实现类添加事务, userMapper添加@cacheNameSpace注解
     * @Author: chenzheng
     * @Date: 2020/2/17 16:35
     * @param: null
     * @Return:
     */
    @Test
    public void testSecondLevelCache() throws InterruptedException {
        System.out.println("使用线程池运行 Runnable 任务：");

        ExecutorService threadPool = Executors.newFixedThreadPool(5); // 创建大小固定为 5 的线程池

        List<Runnable> tasks = new ArrayList<>(10);

        for (int i = 0; i < 3; i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    userTransactionalService.get();
                }
            };

            tasks.add(task);

            threadPool.execute(task); // 让线程池执行任务 task
        }
        threadPool.shutdown(); // 向线程池发送关闭的指令，等到已经提交的任务都执行完毕之后，线程池会关闭

        threadPool.awaitTermination(1, TimeUnit.HOURS); // 等待线程池关闭，等待的最大时间为 1 小时
    }
}
