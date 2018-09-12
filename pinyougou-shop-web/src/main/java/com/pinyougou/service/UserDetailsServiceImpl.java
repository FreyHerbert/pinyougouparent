package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 认证类
 * @author leiyu
 */
public class UserDetailsServiceImpl implements UserDetailsService {
    public static final String SELLER_VERIFY_STATUS = "1";

    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 构建角色列表
        List<GrantedAuthority> grantAuths = new ArrayList<>(16);
        grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        // 根据用户名查找
        TbSeller seller = sellerService.findOne(username);

        if (seller != null && SELLER_VERIFY_STATUS.equals(seller.getStatus())) {
            // 返回用户对象
            return new User(username, seller.getPassword(), grantAuths);
        } else {
            return null;
        }
    }
}
