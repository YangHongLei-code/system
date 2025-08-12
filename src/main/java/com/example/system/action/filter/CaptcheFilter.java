package com.example.system.action.filter;

import com.alibaba.fastjson.JSON;
import com.example.system.action.util.Result;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class CaptcheFilter extends OncePerRequestFilter {
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if("/login".equals(request.getRequestURI())){

            String captcha=request.getParameter("captcha");
            String captchaToken=request.getParameter("captchaToken");


            if(captcha==null || captchaToken==null){

                response.setContentType("application/json;charset=utf-8");
                response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("验证码不正确！").data(new Object()).build()));

            }else {
                String redisCaptcha= (String) redisTemplate.opsForHash().get("Captcha",captchaToken);

                if(captcha.equalsIgnoreCase(redisCaptcha)){
                    filterChain.doFilter(request, response);
                }else{

                    response.setContentType("application/json;charset=utf-8");
                    response.getWriter().write(JSON.toJSONString(Result.builder().code(401).msg("验证码不正确！").data(new Object()).build()));
                }

            }
        }else{
            filterChain.doFilter(request, response);
        }





    }
}
