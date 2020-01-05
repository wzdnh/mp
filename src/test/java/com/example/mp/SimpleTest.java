package com.example.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mp.dao.UserMapper;
import com.example.mp.entity.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;

/**
 * @Author: 帅帅的阿政
 * @Date: 2019/12/24 21:25
 * @description MP入门
 **/

@RunWith(SpringRunner.class)    //运行器
@SpringBootTest
public class SimpleTest {

    @Autowired
    private UserMapper userMapper;

    /**
     * 二 基本使用
     * 1. 通用传统模式简介及通用mapper新增方法
     * 2. 常用注解
     * 3. 排除非表字段的三种方式
     */


    /**
     * 三 MP查询方法
     */

    /**
     * 普通查询
     */
    @Test
    public void select() {
        List<User> list = userMapper.selectList(null);
        //断言
        Assert.assertEquals(5, list.size());
        list.forEach(System.out::println);
    }

    /**
     * 普通查询
     * 根据id查询
     */
    @Test
    public void selectById() {
        User user = userMapper.selectById(1094590409767661570L);
        System.out.println(user.toString());
    }

    /**
     * 普通查询
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
     * 普通查询
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
     * 排除 create_time  manager_id 这两个字段
     * .select()可以写在queryWrapper后面第一个，或者最后一个，不影响
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
        queryWapper
                .like(StringUtils.isNotEmpty(name), "name", name)
                .like(StringUtils.isNotEmpty(email), "email", email);
        userMapper.selectList(queryWapper);
    }

    /**
     * 实体类作为条件构造器的参数
     * 创建条件构造器时，传入实体类对象
     * 默认使用=，使用@TableField(condition=SqlCondition.LIKE)修改为like；
     * 如果有别的需求，可以自己设置
     * "%s&lt;&gt;#{%s}" $s 列名; #{%s} 列的值; &lt;&gt; 表示条件
     * 他与queryWapper使用like,eq等条件互不干扰，所以使用时，要注意不要重复了
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
     * 条件构造器中AllEq用法
     * queryWapper.allEq(params, false); 传入false，表示map中为null的值，字段会被忽略掉
     * queryWapper.allEq((k,v) -> !k.equals("name"), params), 使用lambda，字段name会被过滤掉
     */
    @Test
    public void selectByAllEq() {
        QueryWrapper<User> queryWapper = new QueryWrapper<User>();
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王天风");
        params.put("age", 25);
        queryWapper.allEq(params);
        //queryWapper.allEq(params, false);
        //queryWapper.allEq((k,v) -> !k.equals("name"), params)
        List<User> theUserList = userMapper.selectList(queryWapper);
        theUserList.forEach(System.out::println);
    }


    /**
     * 其他使用条件构造器的方法
     * 其他以条件构造器为参数的查询方法
     * 传入map类型
     * 使用场景1 和 select一起使用，过滤null的字段
     * 只显示id字段和name字段
     */
    @Test
    public void selectByWrapperMaps() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper
                .select("id", "name")
                .like("name", "雨")
                .lt("age", 40);
        /**
         * Map<字段名，值>
         */
        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }

    /**
     * 其他使用条件构造器的方法
     * 其他以条件构造器为参数的查询方法
     * 使用场景2 当查询返回的是统计结果时
     * 需求 按照直属上级分组，查询每组的平均年龄、最大年龄、最小年龄
     * 并且只取年龄总和小于500的组
     * SQL select avg(age) avg_age, min(age) min_age, max(age) max_age
     * from user
     * group by manager_id
     * having sum(age) < 500
     */
    @Test
    public void selectByWrapperMaps2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper
                .select("avg(age) avg_age", "min(age) min_age", "max(age) max_age")
                .groupBy("manager_id")
                //带有可变参数的方法; sqlHaving：使用动态参数的方式构造; {0}:表示获取第一个参数
                .having("sum(age)<{0}", 500);

        List<Map<String, Object>> userList = userMapper.selectMaps(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 其他使用条件构造器的方法
     * 其他以条件构造器为参数的查询方法
     * List<Object> selectObjs()
     * 注意 只返回第一个字段的值，其它的会被舍弃
     * 运行结果 只返回了id字段的值，name字段的值被舍弃
     */
    @Test
    public void selectByWrapperObjects() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper
                .select("id", "name")
                .like("name", "雨")
                .lt("age", 40);

        List<Object> userList = userMapper.selectObjs(queryWrapper);
        userList.forEach(System.out::println);
    }


    /**
     * 其他使用条件构造器的方法
     * 其他以条件构造器为参数的查询方法
     * Integer selectCount() 查总记录数 返回符合条件的总记录数
     * 注意 不能设置要查询的列
     * 运行结果 只返回了id字段的值，name字段的值被舍弃
     */
    @Test
    public void selectByWrapperCount() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper
                .like("name", "雨")
                .lt("age", 40);

        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println("总记录数：" + count);
    }


    /**
     * 其他使用条件构造器的方法
     * 其他以条件构造器为参数的查询方法
     * T selectOne() 根据Entity条件 查寻一条记录
     * 注意 查询的结果只能有一条或者没有，查询的结果有多条时，会报错
     * 运行结果 只返回了id字段的值，name字段的值被舍弃
     */
    @Test
    public void selectByWrapperOne() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper
                .like("name", "刘红雨")
                .lt("age", 40);

        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }


    /**
     * lambda条件构造器 1
     */
    @Test
    public void selectLambda() {
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

    /**
     * lambda条件构造器 2
     */
    @Test
    public void selectLambda2() {
        //3
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.like(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40))
                .or().isNotNull(User::getEmail);
        List<User> lambdaUL = userMapper.selectList(lambdaQuery);
        lambdaUL.forEach(System.out::println);
    }

    /**
     * lambda条件构造器 3
     * 这是mp 3.7新增的方法
     */
    @Test
    public void selectLambda3() {
        List<User> list = new LambdaQueryChainWrapper<User>(userMapper)
                .like(User::getName, "雨")
                .ge(User::getAge, 20)
                .list();
        list.forEach(System.out::println);
    }


    /**
     * 四 自定义SQL及分页
     */

    /**
     * 使用条件构造器的自定义sql(版本大于等于 3.0.7)
     * 自定义方法测试
     * 自定义方法： selectAll()(注解方式)
     */
    @Test
    public void selectMySql1() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery
                .likeRight(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> userList = userMapper.selectAll(lambdaQuery);
        userList.forEach(System.out::println);
    }

    /**
     * 使用条件构造器的自定义sql(版本大于等于 3.0.7)
     * 自定义方法测试
     * 自定义方法： selectAll()(xml方式)
     */
    @Test
    public void selectMySql2() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.<User>lambdaQuery();
        lambdaQuery.likeRight(User::getName, "王")
                .and(lqw -> lqw.lt(User::getAge, 40).or().isNotNull(User::getEmail));
        List<User> lambdaUL = userMapper.selectAll2(lambdaQuery);
        lambdaUL.forEach(System.out::println);
    }

    /**
     * 分页 1
     */
    @Test
    public void selectPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 26);
        //(当前页数，每页条数)
        Page<User> page = new Page<>(1, 4);
        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<User> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }

    /**
     * 分页 2
     * 使用map
     * 有点bug后期在解决了
     */
    @Test
    public void selectPage2() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 26);
        //(当前页数，每页条数)
        Page<User> page = new Page<>(1, 2);
        /*
        IPage<Map<String, Object>> iPage = userMapper.selectMapsPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<Map<String, Object>> userList = iPage.getRecords();
        userList.forEach(System.out::println);
        */
    }


    /**
     * 分页 3
     * 不查总记录数
     */
    @Test
    public void selectPage3() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 26);
        //(当前页数，每页条数)
        Page<User> page = new Page<>(1, 4, false);
        IPage<User> iPage = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<User> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }

    /**
     * 多表联查
     * 通过xml自定义的方式实现
     * .selectUserPage() 自定义方法
     */
    @Test
    public void selectMyPage() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("age", 26);
        //(当前页数，每页条数)
        Page<User> page = new Page<>(1, 4, false);
        IPage<User> iPage = userMapper.selectUserPage(page, queryWrapper);
        System.out.println("总页数" + iPage.getPages());
        System.out.println("总记录数" + iPage.getTotal());
        List<User> userList = iPage.getRecords();
        userList.forEach(System.out::println);
    }

    /**
     * 五 更新删除
     */

    /**
     * 更新方法
     * updateById(T);
     */
    @Test
    public void updateById() {
        User user = new User();
        user.setId(1088248166370832385L);
        user.setAge(26);
        user.setEmail("gai@qq.com");
        Integer rows = userMapper.updateById(user);
        System.out.println("影响记录数" + rows);

    }


    /**
     * 跟新方法
     * update(T, updateWrapper)
     * updateWrapper() 此方法也可以传入一个实体类，实体类不为null的属性会出现在where中
     * 默认条件为=，可以在实体类的相关属性中，通过注解修改
     *
     * @TableField(condition=SqlCondition.LIKE) 注意不要与条件构造器重复
     */
    @Test
    public void updateByWrapper1() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper
                .eq("name", "李艺伟")
                .eq("age", 28);

        User user = new User();
        user.setAge(26);
        user.setEmail("gaiLYW@qq.com");
        Integer rows = userMapper.update(user, updateWrapper);
        System.out.println("影响记录数" + rows);
    }


    /**
     * 更新方法
     * 当只更改对象的一两个属性时，可以不用传入实体类这种方式
     */
    @Test
    public void updateByWrapper2() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper();
        updateWrapper
                .eq("name", "李艺伟")
                .set("age", 30);

        Integer rows = userMapper.update(null, updateWrapper);
        System.out.println("影响记录数" + rows);
    }


    /**
     * 更新方法(lambda)
     * 当只更改对象的一两个属性时，可以不用传入实体类这种方式
     */
    @Test
    public void updateByWrapperLambda() {
        LambdaUpdateWrapper<User> lambdaUpdateWrapper = Wrappers.<User>lambdaUpdate();
        lambdaUpdateWrapper
                .eq(User::getName, "李艺伟")
                .set(User::getAge, 33);

        Integer rows = userMapper.update(null, lambdaUpdateWrapper);
        System.out.println("影响记录数" + rows);
    }

    /**
     * 更新方法(链式lambda)
     * 当只更改对象的一两个属性时，可以不用传入实体类这种方式
     */
    @Test
    public void updateByWrapperLambdaChain() {
        boolean update = new LambdaUpdateChainWrapper<User>(userMapper)
                .eq(User::getName, "李艺伟")
                .eq(User::getAge, 22)
                .set(User::getAge, 33)
                .update();
        System.out.println("影响记录数" + update);
    }


    /**
     * 删除方法
     * 根据id删除
     */
    @Test
    public void deleteById() {
        Integer rows = userMapper.deleteById(1209473313156423686L);
        System.out.println("影响记录数" + rows);
    }


    /**
     * 删除方法
     * 普通删除方法
     * map 默认使用等于
     */
    @Test
    public void deleteByMap() {
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("name", "c");
        Integer rows = userMapper.deleteByMap(columnMap);
        System.out.println("影响记录数" + rows);
    }

    /**
     * 删除方法
     * 普通删除方法
     * 批量删除
     */
    @Test
    public void deleteByBatchIds() {
        Integer rows = userMapper.deleteBatchIds(Arrays.asList(1209473313156423683L, 1209473313156423684l));
        System.out.println("影响记录数" + rows);
    }

    /**
     * 删除方法
     * 带条件构造器删除的方法
     */
    @Test
    public void deleteByWrapperLambda() {
        LambdaQueryWrapper<User> lambdaQueryWrapper = Wrappers.<User>lambdaQuery();
        lambdaQueryWrapper.eq(User::getAge, 50);
        Integer rows = userMapper.delete(lambdaQueryWrapper);
        System.out.println("影响记录数" + rows);
    }



}
