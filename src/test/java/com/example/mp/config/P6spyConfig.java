package com.example.mp.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.springframework.context.annotation.Configuration;

/**
 * <p>[文件描述]：p6spy配置类</p >
 * <p>Copyright (c) 2020 Troila </p >
 * <p>Project Name： mp </p >
 *
 * @author chenzheng
 * @version 1.0, 2020/2/19 14:14
 * @since XTJCB
 */
@Configuration
public class P6spyConfig implements MessageFormattingStrategy {


    @Override
    public String formatMessage(int i, String s, long l, String s1, String s2, String s3, String s4) {
        //return !"".equals(sql.trim()) ? this.format.format(new Date()) + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
        return null;
    }

}
