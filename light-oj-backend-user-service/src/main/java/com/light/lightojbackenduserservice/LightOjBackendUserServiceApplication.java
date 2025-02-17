package com.light.lightojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.light.lightojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.light")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.light.lightojbackendserviceclient.service"})
public class LightOjBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightOjBackendUserServiceApplication.class, args);
    }

}
