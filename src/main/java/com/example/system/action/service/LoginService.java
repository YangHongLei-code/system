package com.example.system.action.service;

import com.example.system.action.dao.LoginDao;

import com.example.system.action.model.Role;
import com.example.system.action.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    LoginDao dao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User projectUser=dao.getUserByAccount(username);

        if(projectUser==null){
            throw new UsernameNotFoundException("账号或密码错误！");
        }
        List<Role> roles= dao.getRoleByUserId(projectUser.getId());
        projectUser.setRoles(roles);

        return projectUser;
    }
}
