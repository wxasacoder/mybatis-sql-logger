package com.biaoguoworks;

import com.alibaba.cloud.nacos.NacosConfigAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author wuxin
 * @date 2025/04/24 21:52:55
 */
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
@MapperScan(basePackages = {"com.biaoguoworks.dao"})
@Import(NacosConfigAutoConfiguration.class)
public class SimpleApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleApplication.class, args);
    }
}