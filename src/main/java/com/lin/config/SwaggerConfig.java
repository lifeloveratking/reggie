package com.lin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    //配置了swagger的bean实例
    @Bean
    public Docket docket(Environment environment) {
        //获取项目环境

        //获取项目的环境
        //environment.acceptsProfiles判断是否自己设定的环境

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("lin y")
                .enable(true) //enable是否启动swagger，如果为false，则swagger不能在浏览器中访问
                .select()
                //RequestHandlerSelectors 指定要扫描的包
                //withMethodAnnotation:扫描方法上的注解
                // basePackage: 扫描指定的包
                .apis(RequestHandlerSelectors.basePackage("com.lin.controller"))
                .build();
    }

    //配置swagger信息 =api info
    public ApiInfo apiInfo() {
        Contact contact = new Contact("lin", "https://miiiss.cn", "2153565368@qq.com");
        ApiInfo apiInfo = new ApiInfo("Api Documentation", "Api Documentation", "1.0", "urn:tos", contact, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
        return apiInfo;

    }
}