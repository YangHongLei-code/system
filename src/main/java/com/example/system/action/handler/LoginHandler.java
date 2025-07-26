package com.example.system.action.handler;


import com.alibaba.fastjson.JSON;
import com.example.system.action.model.User;
import com.example.system.action.util.JwtUtil;
import com.example.system.action.util.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

@Component//登陆处理器
public class LoginHandler implements AuthenticationFailureHandler , AuthenticationSuccessHandler {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.builder().code(500).msg(exception.getMessage()).data(new Object()).build())  );
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user= (User) authentication.getPrincipal();
        String jwt=JwtUtil.generateToken( JSON.toJSONString(user));

        HashOperations hashOperations =redisTemplate.opsForHash();
        hashOperations.put("Token",user.getId(),jwt);


        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(  Result.builder().code(200).msg("操作成功").data(jwt).build()  ));
        //有时会返回{"code":200,"data":{},"msg":"操作成功"}，应该是fastjson2的问题
        //debug启动，出问题的机率高一些

//        System.out.println("实际返回Jwt: " + jwt);
//        System.out.println("实际返回JSON: " + JSON.toJSONString(  Result.builder().code(200).msg("操作成功").data(jwt).build()  ));
//        System.out.println(JSON.toJSONString(  Result.builder().code(200).msg("操作成功").data("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoie1wiYWNjb3VudFwiOlwiYWRtaW5cIixcImFjY291bnROb25FeHBpcmVkXCI6dHJ1ZSxcImFjY291bnROb25Mb2NrZWRcIjp0cnVlLFwiY3JlZGVudGlhbHNOb25FeHBpcmVkXCI6dHJ1ZSxcImRlc2NyaXB0aW9uXCI6XCLnrqHnkIblkZhcIixcImVuYWJsZWRcIjp0cnVlLFwiaWRcIjpcIjFcIixcIm5hbWVcIjpcImFkbWluXCIsXCJwYXNzd29yZFwiOlwiYWRtaW5cIixcInJvbGVzXCI6W3tcImlkXCI6XCIxXCIsXCJyb2xlXCI6XCJhZG1pblwiLFwicm9sZV9uYW1lXCI6XCLnrqHnkIblkZhcIn1dLFwidXNlcm5hbWVcIjpcImFkbWluXCJ9IiwiZXhwIjoxNzUyOTIyNzQxfQ.zmOoqaKR1GnGoUOkCmxve6Bwlg2oRSB8-z247gE3Mww").build()  ));;
    }
}
