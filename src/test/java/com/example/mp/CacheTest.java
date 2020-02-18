package com.example.mp;

import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import com.example.mp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * <p>[文件描述]：缓存测试，事务测试</p >
 * @author chenzheng
 */
@Slf4j
@RunWith(SpringRunner.class)    //运行器
@SpringBootTest
public class CacheTest {

    @Autowired
    private UserMapper userMapper;
    @Qualifier("userService")
    @Autowired
    private UserService userService;
    @Autowired
    private SqlSessionFactory factory;

    /**
     * @Description: 查看缓存状态
     * @Author: chenzheng
     * @Date: 2020/2/18 16:20
     * @Return: void
     */
    @Test
    public void showDefaultCacheConfiguration() {
        System.out.println("一级缓存范围: " + factory.getConfiguration().getLocalCacheScope());
        System.out.println("二级缓存是否被启用: " + factory.getConfiguration().isCacheEnabled());
    }

    /**
     * @Description: 单线程环境测试一级缓存
     * Creating a new SqlSession 3次
     * JDBC Connection 1次
     * Fetching JDBC Connection from DataSource 2次
     * sql执行了3次
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
     * @Description: 单线程环境测试二级缓存
     * Creating a new SqlSession 1次
     * fetched sqlsession 2次
     * JDBC Connection 1次
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test2() {
        for (int i = 0; i < 3; i++) {
            userService.get();
        }
    }


    /**
     * @Description: 多线程环境测试一级缓存
     * @Author: chenzheng
     * @Date: 2020/2/18 12:26
     * @Return: void
     */
    @Test
    public void test4() throws InterruptedException {
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
     * @Description: 多线程环境测试二级缓存, userMapper添加@cacheNameSpace注解
     * create sql session 3次
     * jdbc connection 3次
     * @Author: chenzheng
     * @Date: 2020/2/17 16:35
     * @param: null
     * @Return:
     */
    @Test
    public void test5() throws InterruptedException {
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
     * @Description: 测试事务回滚
     * @Author: chenzheng
     * @Date: 2020/2/18 14:50
     * @Return: void
     */
    @Transactional(value = "transactionManager",
            isolation = Isolation.DEFAULT,
            propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class,
            timeout=36000)
    @Test
    public void testRollback(){
        User user1 = new User();
        user1.setName("cishi1");
        userService.addUser(user1);
        User user2 = new User();
        user2.setId(1087982257332887553L);
        user2.setName("ceshi2");
        userService.addUser(user2);
    }

}
