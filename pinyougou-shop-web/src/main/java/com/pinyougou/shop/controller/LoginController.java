package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
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

    @Reference
    private SellerService sellerService;

    @RequestMapping("/getProfile")
    public Map<String, String> getProfile() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        TbSeller seller = sellerService.findOne(name);
        Map<String, String> map = new HashMap<>(16);
        map.put("username", seller.getName());
        map.put("nickName", seller.getNickName());
        return map;
    }
}
