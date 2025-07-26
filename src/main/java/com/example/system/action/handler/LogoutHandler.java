package com.example.system.action.handler;


import com.alibaba.fastjson.JSON;
import com.example.system.action.model.User;
import com.example.system.action.util.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;

import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LogoutHandler implements LogoutSuccessHandler {

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HashOperations hashOperations  =  redisTemplate.opsForHash();
        User user = (User) authentication.getPrincipal();
        hashOperations.delete("Token",user.getId() );

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.builder().code(200).msg("操作成功").data(new Object()).build())  );
    }
}
