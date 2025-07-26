package com.example.system.action.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class CaptcheFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if("login".equals(request.getRequestURI())){

            filterChain.doFilter(request, response);

//            String captche= (String) request.getParameter("captche");
//            if(captche==null){
//                response.sendRedirect(request.getContextPath()+"/login");
//            }else if(captche.equals(request.getAttribute("CAPTCHA"))){
//                filterChain.doFilter(request, response);
//            }else{
//                response.sendRedirect(request.getContextPath()+"/login");
//            }
        }else{
            filterChain.doFilter(request, response);
        }





    }
}
