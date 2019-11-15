package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:
 */

@EnableEurekaClient
@SpringBootApplication
@MapperScan("com.leyou.mapper")
public class LyItemServiceApplication {

    public static void main(String[] args){
        SpringApplication.run(LyItemServiceApplication.class);
    }
}
