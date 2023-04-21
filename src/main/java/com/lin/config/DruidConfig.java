package com.lin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDatasource(){

        return new DruidDataSource();
    }
    //实现后台监控功能 相当于web.xml,由于spring内置了servlet,druid相当于这个
    @Bean
    public ServletRegistrationBean a(){
        ServletRegistrationBean<StatViewServlet>bean=new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");
        //后端需要有人登陆
        HashMap<String, String> init= new HashMap<>();
        //增加配置 登录key是固定的loginUsername
        init.put("loginUsername","admin");
        init.put("loginPassword","123456");


        //允许谁可以访问
        init.put("allow","");
        //禁止allow访问
//        init.put("allow","ip地址");
        bean.setInitParameters(init);
        return bean;
    }
    //filter
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new WebStatFilter());
        //可以过滤那些请求
        HashMap<String, String> init= new HashMap<>();
        init.put("exclusions","*.js,*.css,/druid/*");
        //这些东西不进行统计
        bean.setInitParameters(init);
        return bean;
    }

}
