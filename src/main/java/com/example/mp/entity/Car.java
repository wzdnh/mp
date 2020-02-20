package com.example.mp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>[文件描述]：汽车表</p >
 * @author chenzheng 2020/2/19 11:26
 */

@Data
public class Car {
    @TableId
    long id;
    /**
     * 车牌号
     */
    String name;
}
