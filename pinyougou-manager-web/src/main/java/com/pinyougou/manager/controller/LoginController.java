package com.pinyougou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 *  登陆控制器
 * @author leiyu
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/name")
    public Map<String, String> name() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap(16);
        map.put("loginName", username);
        return map;
    }
}
