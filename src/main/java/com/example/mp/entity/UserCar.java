package com.example.mp.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>[文件描述]：用户汽车关系表</p >
 * <p>Copyright (c) 2020 Troila </p >
 * <p>Project Name： mp </p >
 *
 * @author chenzheng
 * @version 1.0, 2020/2/19 11:29
 * @since XTJCB
 */

@Data
public class UserCar {
    @TableId
    long id;
    long uid;
    long cid;
}
