package com.example.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 帅帅的阿政
 * @Date: 2019/12/24 21:25
 **/

@RunWith(SpringRunner.class)    //运行器
@SpringBootTest
public class SimpleTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 查询
     */
    @Test
    public void select() {

        /**
         * @param: queryWrapper 条件构造器
         */
        List<User> list = userMapper.selectList(null);
        //断言
        Assert.assertEquals(5, list.size());
        list.forEach(System.out::println);
    }

    /**
     * 根据id查询
     */
    @Test
    public void selectById() {
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user.toString());
    }

    /**
     * 根据id list查询多个
     */
    @Test
    public void selectByIdList() {
        List<Long> idList = Arrays.asList(1094590409767661570L,
                1094592041087729666L);
        List<User> userList = userMapper.selectBatchIds(idList);
        //1094590409767661570L);
        userList.forEach(System.out::println);
    }

    /**
     * 根据map查询
     * key:表字段名; value: 表字段的值
     */
    @Test
    public void selectByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "王天风");
        columnMap.put("age", 25);
        List<User> userList = userMapper.selectByMap(columnMap);
    }

    /**
     * 根据条件构造器QueryWrapper 查询
     * 如 like eq
     */
    @Test
    public void selectByWrapper() {
        QueryWrapper<User> queryWapper = new QueryWrapper<User>();
        queryWapper
                .like("name", "雨")
                .lt("age", 40);
        userMapper.selectList(queryWapper);
    }

    /**
     * selec不列出全部字段 1
     */
    @Test
    public void selectByWrapper2() {
        QueryWrapper<User> queryWapper = new QueryWrapper<User>();
        queryWapper
                .select("id", "name")
                .like("name", "雨")
                .lt("age", 40);
        userMapper.selectList(queryWapper);
    }

    /**
     * selec不列出全部字段 2
     */
    @Test
    public void selectByWrapper3() {
        QueryWrapper<User> queryWapper = new QueryWrapper<User>();
        queryWapper
                .like("name", "雨")
                .lt("age", 40)
                .select(User.class, info -> !info.getColumn().equals("create_time") &&
                        !info.getColumn().equals("manager_id")
                );
        userMapper.selectList(queryWapper);
    }


    /**
     * 插入
     * 默认策略：1实体类的变量为空时，插入，修改时 不会使用此列
     * 2 会自动添加id， 默认采用雪花算法自增id
     * 3 默认下划线、驼峰、匹配
     * 4 mybatis-plus 默认使用实体类属性名为id的作为主键，如果主键是
     * 别的名字，需要使用注解指定id
     */
    @Test
    public void insert() {
        User user = new User();
        user.setName("刘明强");
        user.setAge(3);
        user.setManagerId(1088248166370832385L);
        user.setCreateTime(LocalDateTime.now());
        /**
         * insert() 返回的是影响的记录数
         */
        int rows = userMapper.insert(user);
        System.out.println("返回的是影响的记录数" + rows);
    }


    /**
     * condition(条件) 的作用
     */
    @Test
    public void testCondition() {
        String name = "王";
        String email = "";
        condition(name, email);
    }

    private void condition(String name, String email) {
        QueryWrapper<User> queryWapper = new QueryWrapper<User>();

        // .like(condition, column, value)
        queryWapper
                .like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        userMapper.selectList(queryWapper);
    }

    /**
     * 实体类作为条件构造器的参数，默认使用=，使用@TableField(condition=SqlCondition.LIKE)修改为like；
     * 如果有别的需求，可以自己设置
     * "%s&lt;&gt;#{%s}" $s 列名; #{%s} 列的值; &lt;&gt; 表示条件
     * 他与queryWapper使用like,eq等条件互不干扰
     * 所以使用时，要注意不要重复了
     * QueryWrapper(T entity)
     */
    @Test
    public void selectByWrapper4() {
        User whereUser = new User();
        whereUser.setName("刘红雨");
        whereUser.setAge(32);

        QueryWrapper<User> queryWapper = new QueryWrapper<User>(whereUser);

        List<User> theUserList = userMapper.selectList(queryWapper);
        theUserList.forEach(System.out::println);
    }

    /**
     * lambda
     */
    @Test
    public void selectLambda(){
        //1
        LambdaQueryWrapper<User> lambda = new QueryWrapper<User>().lambda();
        //2
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //3
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.like(User::getName, "雨").lt(User::getAge, 40);
        List<User> lambdaUL = userMapper.selectList(lambdaQuery);
        lambdaUL.forEach(System.out::println);
    }

    @Test
    public void selectLambda2(){
        //3
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.like(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40))
                .or().isNotNull(User::getEmail);
        List<User> lambdaUL = userMapper.selectList(lambdaQuery);
        lambdaUL.forEach(System.out::println);
    }

    /**
     * 3.7新增的方法
     */
    @Test
    public void selectLambda3(){
        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getName, "雨")
                .ge(User::getAge, 20)
                .list();
        list.forEach(System.out::println);
    }

    /**
     * 自定义方法测试
     */
    @Test
    public void selectLambda4(){
        //3
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.like(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40))
                .or().isNotNull(User::getEmail);
        List<User> lambdaUL = userMapper.selectAll(lambdaQuery);
        lambdaUL.forEach(System.out::println);
    }
}
