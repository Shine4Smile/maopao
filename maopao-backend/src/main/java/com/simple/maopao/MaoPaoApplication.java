package com.simple.maopao;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 启动类
 *
 * @author Simple
 */
//@EnableRedisHttpSession
@SpringBootApplication
@MapperScan("com.simple.maopao.mapper")
@EnableScheduling
public class MaoPaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaoPaoApplication.class, args);
    }

}
