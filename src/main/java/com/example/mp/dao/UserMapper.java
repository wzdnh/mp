package com.example.mp.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mp.entity.User;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: 帅帅的阿政
 * @Date: 2019/12/24 21:23
 * @description 使用条件构造器的自定义方法
 **/
//@CacheNamespace
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 自定义方法 注解方式
     * @Param(Constants.WRAPPER) ： 传入一个querywrapper对象
     */
    @Select("select * from user ${ew.customSqlSegment}")
    List<User> selectAll(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * 自定义方法 xml方式
     * @return
     */
    List<User> selectAll2(@Param(Constants.WRAPPER) Wrapper<User> wrapper);

    /**
     * 多表联查
     * @return
     */
    IPage<User> selectUserPage(Page<User> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
