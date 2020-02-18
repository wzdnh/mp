package com.example.mp.entity;

import com.baomidou.mybatisplus.annotation.SqlCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Condition;

/**
 * @Author: 帅帅的阿政
 * @Date: 2019/12/24 21:19
 **/

@Data
public class User implements Serializable {

    @TableId
    private Long id;
    @TableField(condition= "%s&lt;&gt;#{%s}")
    private String name;
    private Integer age;
    private String email;
    private Long managerId;
    private LocalDateTime createTime;

}
