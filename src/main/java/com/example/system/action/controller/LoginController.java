package com.example.system.action.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.example.system.action.model.User;
import com.example.system.action.service.LoginService;
import com.example.system.action.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Principal;

@RestController
@CrossOrigin
public class LoginController {
    //获取验证码
    @RequestMapping("/captche")
    public void captche(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setContentType("image/jpeg");

        CircleCaptcha circleCaptcha= CaptchaUtil.createCircleCaptcha(150,30,4,10,1);

        String captche=circleCaptcha.getCode();
        request.getSession().setAttribute("CAPTCHA", captche);

        circleCaptcha.write(response.getOutputStream());
    }
    //获取登陆用户信息
    @RequestMapping("/userinfo")
    public Object userinfo(){

        return Result.builder().code(200).msg("success").data(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).build() ;
    }

    //测试角色权限访问
    @PreAuthorize(value="hasAnyAuthority('admin')")
    @RequestMapping("/rolePermission")
    public Object rolePermission(){

        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
