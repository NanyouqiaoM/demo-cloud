package com.example.servers.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@RefreshScope
public class UserController {
    @Value("${user.name}")
    private String userName;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/get")
    public String get() {
        System.out.println(userName + "===");
        return userName;
    }

    @GetMapping("/info")
    public User info(@RequestParam String accessToken) {
        System.out.println("token = " + accessToken);
        if (accessToken.equals("abc")) {
            return new User(1, "张三", 20);
        }
        throw new RuntimeException("用户未登录");

    }


    static class User {

        private int id;

        private String name;

        private int age;

        public User(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
