package com.hang.reiji;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching //可以使用缓存注解
@SpringBootApplication  //启动类注解，配置作用，
@ServletComponentScan  //大概是扫描@Service注解
@MapperScan(basePackages = "com.hang.reiji.mapper")  //指定mapper接口的位置
public class ReijiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReijiApplication.class, args);
    }

}
