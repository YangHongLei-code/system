package com.example.system.action.filter;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.system.action.model.User;
import com.example.system.action.util.JwtUtil;
import com.example.system.action.util.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

// 自定义过滤器
@Component
public class JwtCookieFilter extends OncePerRequestFilter {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if("/login".equals(requestURI)){
            chain.doFilter(request, response);
        }else{
            String jwtToken=request.getHeader("jwtToken");
            if(jwtToken!=null&&!jwtToken.isEmpty()){
                try {
                    JwtUtil.verifyToken(jwtToken);//验证token合法性

                    User user= JSON.parseObject(JwtUtil.parseToken(jwtToken), User.class);
                    String redisToken= (String) redisTemplate.opsForHash().get("Token",user.getId());

                    if(jwtToken.equals(redisToken)){
                        UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        chain.doFilter(request, response);
                    }else{
                        response.setContentType("application/json;charset=utf-8");
                        response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("用户状态变更，请重新登录！").data(new Object()).build()));
                    }

                } catch (TokenExpiredException e) {
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("登录超时！").data(new Object()).build()));
                } catch (Exception e) {
                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("登录环境异常！").data(new Object()).build()));
                    e.printStackTrace();
                }

            }else{
                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("登录环境异常！").data(new Object()).build()));
            }
        }


    }
}