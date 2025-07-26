package com.example.system.action.handler;

import com.alibaba.fastjson.JSON;
import com.example.system.action.model.User;
import com.example.system.action.util.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component//权限处理
public class PermissionHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {


        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.builder().code(403).msg("权限不足，联系管理员").data(new Object()).build())  );
    }
}
