package com.ibicd.promote;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ibicd.promote.mybatis.mapper")//用于扫描Mybatis接口的路径
public class PromoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromoteApplication.class, args);
    }

}
