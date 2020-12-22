package com.wu.springsecuritydemo.controller;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @Author: Wgy
* @date: 2020/7/22 10:13
* Description:用户控制器
*/
@RestController
public class UserController {



    @RequestMapping(value = "/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping(value = "/admin/hello")
    public String adminHello(){
        return "adminHello";
    }

    @RequestMapping(value = "/user/hello")
    public String userHello(){
        return "userHello";
    }




}
