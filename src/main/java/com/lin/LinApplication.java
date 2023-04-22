package com.lin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableCaching
@EnableTransactionManagement(proxyTargetClass=true)
public class LinApplication {
    public static void main(String[] args) {
        SpringApplication.run(LinApplication.class,args);
        log.info("项目已启动成功");
    }
}
